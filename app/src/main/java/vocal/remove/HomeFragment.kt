package vocal.remove

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vocal.remove.adapter.SongAdapter
import vocal.remove.databinding.FragmentHomeBinding
import vocal.remove.models.AudioModel
import vocal.remove.models.Music
import vocal.remove.service.SpleeterService

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment(), SongAdapter.setOnSongCLick {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        initListeners()
        if (isPermissionAccepted()) {
            poulateSongsFromStorage()
        }


        return view
    }

    private fun poulateSongsFromStorage() {
        val allAudioFromDevice = getAllAudioFilesFromDevice(requireContext())
        binding.rvSongs.layoutManager =
            LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        val adapter = SongAdapter(allAudioFromDevice as ArrayList<AudioModel>, this)
        binding.rvSongs.adapter = adapter
    }

    private fun isPermissionAccepted(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied")
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                111
            )
            return false
        }

        return true
    }


    private fun initListeners() {
        binding.btnExtract.setOnClickListener { startExtractionService() }
    }

    private fun startExtractionService() {
        Toast.makeText(requireActivity(), "Background service started", Toast.LENGTH_LONG).show()

        var intentService: Intent =
            Intent(requireActivity(), SpleeterService("SpleeterService")::class.java)
        intentService.putExtra("anything", "pass data to background service")
        requireActivity().startService(intentService)
    }



    fun getAllAudioFilesFromDevice(context: Context): List<AudioModel>? {
        val tempAudioList: MutableList<AudioModel> = ArrayList()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val c = context.contentResolver.query(
            uri,
            projection,
            MediaStore.Audio.Media.DATA + " like ? ",
            arrayOf("%a%"),
            null
        )
        if (c != null) {
            while (c.moveToNext()) {

                var path = c.getString(0)
                var name = c.getString(1)
                var album = c.getString(2)
                var artist = c.getString(3)
                val audioModel = AudioModel(path, name, album, artist)
                Log.e("Name :$name", " Album :$album")
                Log.e("Path :$path", " Artist :$artist")
                tempAudioList.add(audioModel)
            }
            c.close()
        }
        return tempAudioList
    }

    companion object {
        private const val TAG = "HomeFragment"
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            111 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show()
                        poulateSongsFromStorage()
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onSongClick(aPath: String) {
        Toast.makeText(requireActivity(), aPath, Toast.LENGTH_LONG).show()
    }

}