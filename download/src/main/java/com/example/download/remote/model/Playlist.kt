package com.example.download.remote.model

import com.google.gson.annotations.SerializedName

data class Playlist(
    @SerializedName("playlist")
    val manifest: Data?,
    @SerializedName("resolutions")
    val videos: List<Data>,
    val segment: Data?,
    val key: Data?,
    val subtitles: List<Data>,
    val size: Long = 0
) {
    data class Data(
        val name: String?,
        val url: String?
    )
}