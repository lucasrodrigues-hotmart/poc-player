package com.example.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.*
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

private const val JOB_ID = 1
private const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"

class PocDownloadService: DownloadService(1) {
    private val downloadNotificationHelper: DownloadNotificationHelper by lazy {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(DOWNLOAD_NOTIFICATION_CHANNEL_ID, "download")
        }

        DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
    }

    override fun getDownloadManager(): DownloadManager {
        return DownloadManagerBuilder.getDownloadManager(this)
    }

    override fun getForegroundNotification(downloads: MutableList<Download>): Notification {
        return downloadNotificationHelper.buildProgressNotification(
            android.R.drawable.arrow_down_float, null, null, downloads);
    }

    override fun getScheduler(): Scheduler? {
        return PlatformScheduler(this, JOB_ID)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}