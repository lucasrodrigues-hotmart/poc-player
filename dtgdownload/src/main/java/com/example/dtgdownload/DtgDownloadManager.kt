package com.example.dtgdownload

import android.content.Context
import android.util.Log
import com.kaltura.dtg.ContentManager
import com.kaltura.dtg.DownloadItem
import com.kaltura.dtg.DownloadStateListener
import java.io.IOException

typealias DtgDownloadListener = (percent: Long) -> Unit

class DtgDownloadManager(context: Context) {
    private val contentManager: ContentManager by lazy {
        ContentManager.getInstance(context).apply {
            settings.defaultHlsAudioBitrateEstimation = 64000
            settings.applicationName = "PocPlayer"
            settings.maxConcurrentDownloads = 4
            settings.createNoMediaFileInDownloadsDir = true

            addDownloadStateListener(downloadStateListener)
        }
    }

    private val downloadStateListener = object : DownloadStateListener {
        override fun onDownloadComplete(item: DownloadItem?) {
            Log.e("aaaa", "download complete: ${item?.itemId}")
        }

        override fun onDownloadStart(item: DownloadItem?) {
            Log.e("aaaa", "download start: ${item?.itemId}")
        }

        override fun onDownloadPause(item: DownloadItem?) {
            Log.e("aaaa", "download pause: ${item?.itemId}")
        }

        override fun onDownloadMetadata(item: DownloadItem?, error: Exception?) {
            Log.e("aaaa", "download metadata: ${item?.itemId}")
            item?.startDownload()
            error?.printStackTrace()
        }

        override fun onProgressChange(item: DownloadItem?, downloadedBytes: Long) {
            Log.e("aaaa", "progress - downloaded: ${item?.downloadedSizeBytes}, estimated: ${item?.estimatedSizeBytes}")
            listener?.invoke(((item?.downloadedSizeBytes ?: 0) * 100) / (item?.estimatedSizeBytes ?: 1))
        }

        override fun onDownloadFailure(item: DownloadItem?, error: Exception?) {
            Log.e("aaaa", "download failure: ${item?.itemId}")
        }

        override fun onTracksAvailable(
            item: DownloadItem?,
            trackSelector: DownloadItem.TrackSelector?
        ) {
            Log.e("aaaa", "tracks available: ${item?.itemId}")
        }
    }

    var listener: DtgDownloadListener? = null

    fun start(block: () -> Unit) {
        try {
            contentManager.start {
                block.invoke()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun download(mediaCode: String, contentUri: String) {
        try {
            if (contentManager.findItem(mediaCode) != null) {
                contentManager.removeItem(mediaCode)
            }

            val item = contentManager.createItem(mediaCode, contentUri)
            item.loadMetadata()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace();
        } catch (e: IllegalStateException) {
            e.printStackTrace();
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }
}