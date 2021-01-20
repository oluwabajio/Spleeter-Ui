package vocal.remove.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vocal.remove.R
import vocal.remove.models.AudioModel

public class SongAdapter(val songList: ArrayList<AudioModel>, val  songCLick: setOnSongCLick) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {


    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_song, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        holder.bindItems(songList[position], songCLick)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return songList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(song: AudioModel, songCLick: setOnSongCLick) {
            val textViewName = itemView.findViewById(R.id.tvSongTitle) as TextView
            textViewName.text = song.aName

                itemView.setOnClickListener {
                    Log.e("TAG", "bindItems: "+ song.aName )
                    songCLick.onSongClick(song.aPath)

                }


        }
    }

    interface setOnSongCLick {
        fun onSongClick(aPath: String)
    }
}
 
