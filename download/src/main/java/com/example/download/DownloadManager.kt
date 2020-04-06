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
        val downloadRequest = DownloadRequest(
            mediaCode,
            DownloadRequest.TYPE_PROGRESSIVE,
            Uri.parse(contentUri),  /* streamKeys= */
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
                listeners.forEach { listener ->
                    listener.invoke(download.percentDownloaded)
                }
            }
        })
    }
}