package com.example.download.remote.api

import com.example.download.remote.model.Playlist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface PlaylistApi {
    @GET("{mediaCode}/offline")
    suspend fun getPlaylist(
        @Path("mediaCode") mediaCode: String,
        @Header("Authorization") authorization: String,
        @Header("Club") productIdentifier: String
    ): Response<Playlist>
}