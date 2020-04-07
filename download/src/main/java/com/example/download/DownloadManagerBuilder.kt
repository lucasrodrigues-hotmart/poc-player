package com.example.download

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

private const val DOWNLOAD_CONTENT_DIRECTORY = "pocdownloads"

object DownloadManagerBuilder {
    private var downloadManager: DownloadManager? = null
    private var downloadCache: SimpleCache? = null
    private var databaseProvider: ExoDatabaseProvider? = null

    fun getDownloadManager(context: Context): DownloadManager {
        if (downloadManager == null) {
            downloadManager = DownloadManager(
                context,
                getDatabaseProvider(context),
                getDownloadCache(context),
                DefaultHttpDataSourceFactory("PocPlayer")
            )
        }

        return downloadManager!!
    }

    fun getDownloadCache(context: Context): SimpleCache {
        if (downloadCache == null) {
            val directory = File(context.filesDir, DOWNLOAD_CONTENT_DIRECTORY)
            downloadCache = SimpleCache(directory, NoOpCacheEvictor(), getDatabaseProvider(context))
        }

        return downloadCache!!
    }

    private fun getDatabaseProvider(context: Context): ExoDatabaseProvider {
        if (databaseProvider == null) {
            databaseProvider = ExoDatabaseProvider(context)
        }

        return databaseProvider!!
    }
}