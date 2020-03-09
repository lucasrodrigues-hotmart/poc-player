package com.hotmart.sparkle.pocplayer

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.hotmart.sparkle.pocplayer.remote.BaseOkHttpClient
import com.hotmart.sparkle.pocplayer.remote.VideoRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://api.sparkleapp.com.br/rest/v2/news/243410/download"
        val token = "H4sIAAAAAAAAAHVUa5OiOhD9RbPFc9CPKg%2BDJhEMgeTLFA9HiOA4Cwrk12%2BcW7t3auveD6lK0af7nO4%2B4TSHdRGUDW7CYyKBjhrQg2tslxvwCi63jG7C5Y%2FTHOqlQefMsOsiTZ7gvkpRXc72XBjxnKeVzjO0yoOlVqT%2BXG1sURiagwKo4TTumEgGHMQ1ajQNuaW2Jxedu2xAHTORTGYe8JZ3yQyasSkN9CgDxdH2TdnRmm%2Begqo5z%2BIWtFoPuvpRbfQvPUB8NJAAichKh%2FLskC0VLINnmqKP0Ylr4IcPvg1v3KRtKfsJu%2BwORWTthWfCxtKx209wY%2F05yF05xT817mW3vJRde61SKqNAcQZ6XV6jOzOWAzYmnRm%2B%2FI7507uqiUhiQwlm7MIZuqsRa%2BHMMnQr%2Fismh3UVtEYhh4j6y00ieod3cOZpYsEUDmp2EzxqGidg2qdAYykYEFH6g0hT%2Barvi42lpyNykVAAS9UekTyPe3FW%2FXgaJpGNBDAVrwHJ2YFfGs5SYUwoVyYUK4nNNqNti%2FMu1mOT5rmHRtr54ihqXm5LgwQ3lkvfLAIuYEdb7PpCadEh8QYUJDM86g0K%2FGZPkgnJ9UXNcUQE2pj0DpbPAyzk%2Fr73zX4TtqftqsHCUztgSovS7pZqt75eqVmrmWq5mikQtwK0KIpouCcefacU%2BcnToN%2B%2BRckUHT3%2F31iHHoXSUphhzYPll6eqjt55QC%2BFCZ7c43NvVRY%2Fyr9qUc1XLdEk07gf0Zgml%2BSJV17WVY7%2FO58knn88apOf6WuFQSBS3H%2FlfMPEIE70%2F49roU%2B%2F679SybPwnV1Bkx3HJr%2FGt6fPUcCU57wZiXLgrnLIRtMYAfM%2B9VvkAuUTKqABLZaGNZLAePbNjOnGU1vLjPbyVUNUHSZwhK434DSSUL1HJldSvUcLp96gvDBiQhsmecfU%2FsFV%2BxGCh8DDx5wOwtmu2jl%2FzY9VmD9Krboye%2BstbGKsjztdvk5xsBsP5ZHDz5m%2FnaMWWi8Puq0Pfr590CXEVlaeD8x0JPdfKbtZwTgXLvW7rVEkTp%2Ff3PPPff0zgIdjdXvL0pi%2F894eeptsjZ%2F7%2B3sq2D40ssFJ4MvdNezryREdOad0me4uHE4TPPePJciHl6iNRu1t0bSsPu1gMNgZvi2DXfQhSYaMKWnBfjB3PgIecEf%2B%2FrpzlotWhqfeLqWfhs6YxWHi67fHHe8IjSuv%2FYSLQ3pwt90LRgsPffSZaYm6j7bWMq4zd1cOzWewS1%2Fp2VG%2FOMeZLqeWLCh92Z3e1of1lVovq7FyLp%2BxeR9YsfoFNuFCmHcFAAA%3D"

        VideoRequest.perform(this, url, token) { parsedUrl ->
            runOnUiThread {
                playMedia(parsedUrl)
            }
        }
    }

    private fun playMedia(mediaUrl : String) {
        val dataSourceFactory: DataSource.Factory = OkHttpDataSourceFactory(BaseOkHttpClient.getOkHttpClient(this), "poc")

        // This is the MediaSource representing the media to be played.
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(Uri.parse(mediaUrl))

        // Prepare the player with the source.
        val simpleExoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = true
            prepare(videoSource)
        }

        playerView.player = simpleExoPlayer
    }
}
