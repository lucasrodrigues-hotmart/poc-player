package com.example.download

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

private const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"

object DownloadManagerBuilder {
    private var downloadManager: DownloadManager? = null

    fun getDownloadManager(context: Context): DownloadManager {
        if (downloadManager == null) {
            val databaseProvider = ExoDatabaseProvider(context)
            val directory = File(context.filesDir, DOWNLOAD_CONTENT_DIRECTORY)
            val downloadCache = SimpleCache(directory, NoOpCacheEvictor(), databaseProvider)
            downloadManager = DownloadManager(
                context,
                databaseProvider,
                downloadCache,
                DefaultHttpDataSourceFactory("PocPlayer")
            )
        }

        return downloadManager!!
    }
}