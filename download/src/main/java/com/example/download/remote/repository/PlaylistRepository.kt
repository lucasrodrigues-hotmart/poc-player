package com.example.download.remote.repository

import com.example.download.remote.model.Playlist

interface PlaylistRepository {
    @Throws(Exception::class)
    suspend fun getPlaylist(
        mediaCode: String,
        authorization: String,
        productIdentifier: String
    ): Playlist
}