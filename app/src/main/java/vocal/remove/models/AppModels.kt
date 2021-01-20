package vocal.remove.models

import java.sql.Struct

data class Music(val id: Long, val title: String, val path: String)

data class AudioModel(
    val aPath: String, val aName: String, val aAlbum: String, val aArtist: String
)