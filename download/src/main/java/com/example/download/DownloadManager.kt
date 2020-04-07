package com.example.download

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService

typealias DownloadListener = (percent: Float) -> Unit

object DownloadManager {
    private val listeners = mutableListOf<DownloadListener>()

    fun addListener(listener: DownloadListener) {
        listeners.add(listener)
    }

    fun download(context: Context, mediaCode: String, contentUri: String) {
        Log.e("aaaa", "should start download of $mediaCode")
        Log.e("aaaa", "Uri: $contentUri")

        val downloadRequest = DownloadRequest(
            mediaCode,
            DownloadRequest.TYPE_HLS,
            Uri.parse("https://s3.amazonaws.com/player.hotmart.dev/N4Rzm4PWZV/720/720.m3u8"),
            emptyList(),
            null,
            null
        )

        DownloadService.sendAddDownload(
            context,
            PocDownloadService::class.java,
            downloadRequest,
            false
        )

        val manager = DownloadManagerBuilder.getDownloadManager(context)
        manager.addListener(object : DownloadManager.Listener {
            override fun onInitialized(downloadManager: DownloadManager) {
                Log.e("aaaa", "on initialized")
            }

            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download
            ) {
                Log.e("aaaa", "on download changed")

                listeners.forEach { listener ->
                    listener.invoke(download.percentDownloaded)
                }
            }

            override fun onIdle(downloadManager: DownloadManager) {
                Log.e("aaaa", "on idle")
            }


        })
    }
}