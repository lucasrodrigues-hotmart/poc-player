package com.example.download.remote.repository

import com.example.download.remote.api.PlaylistApi
import com.example.download.remote.base.Outcome
import com.example.download.remote.base.ext.parseResponse
import com.example.download.remote.model.Playlist

class PlaylistDataRepository(private val playlistApi: PlaylistApi): PlaylistRepository {
    override suspend fun getPlaylist(
        mediaCode: String,
        authorization: String,
        productIdentifier: String
    ): Playlist {
        val outcome = playlistApi.getPlaylist(mediaCode, authorization, productIdentifier)
            .parseResponse()

        when(outcome) {
            is Outcome.Success -> return outcome.value
            is Outcome.Failure -> throw outcome.throwable
        }
    }
}