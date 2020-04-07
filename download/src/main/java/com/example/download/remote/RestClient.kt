package com.example.download.remote

import com.example.download.remote.api.PlaylistApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {
    private val playlistApi = Retrofit.Builder()
        .baseUrl("https://api-club.buildstaging.com/content/v5/player/media/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PlaylistApi::class.java)

    fun getPlaylistApi(): PlaylistApi {
        return playlistApi
    }
}