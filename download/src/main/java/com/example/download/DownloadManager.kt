package com.example.download

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService

object DownloadManager {
    fun download(context: Context, mediaCode: String, contentUri: String) {
        val downloadRequest = DownloadRequest(
            mediaCode,
            DownloadRequest.TYPE_PROGRESSIVE,
            Uri.parse(contentUri),  /* streamKeys= */
            emptyList(),
            null,
            null
        )

        DownloadService.sendAddDownload(context, PocDownloadService::class.java, downloadRequest, false)
    }
}