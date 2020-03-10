package com.hotmart.sparkle.pocplayer

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.TransferListener
import com.hotmart.sparkle.pocplayer.remote.BaseOkHttpClient
import com.hotmart.sparkle.pocplayer.remote.VideoRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock

@ExperimentalTime
class MainActivity : AppCompatActivity() {
    val time = MonoClock.markNow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://api.sparkleapp.com.br/rest/v2/news/207274/download"
//        val url = "https://api.sparkleapp.com.br/rest/v2/news/243410/download"
        val token = "H4sIAAAAAAAAAIVTy46rOBT8ohnx7CRLEgiBxnY7GPPYRDw6AQNpEiBgf%2F11ruZKsxhpFkeyzeFUuar8zf26cMsGNX4YCU%2BFjTd697NZHrwPrx0SevB3f39zXy01yhPNrIs4ejeLKtm%2Fyvu5wye4pIn%2FLFxalydrTrXdFPQ%2BRwJzFDsajD0exFikmjNB%2B6bBUDERcZaAYAXZ%2Bx7ZWEsJWNDhDQxZoe87jw2Fd%2Fe7NMZNcPD1su%2BULPQruf4pdDxVCezKRhJkjgIJ4Ii0BlKhWro7XrnRLdL3ddlPY0bGFYpyhqydAXPWgHk6EhKb%2FS4uawVs%2Bv%2B76H9mU1G6R55ra5cm%2B0XOUqGw5IxIBba1yNrgu1%2BXWjf%2B1zekHJU8yV5I3UeJ4odIjGvGuhqymwBSF8AsQ%2BqyZDGeUgZrwBUFapEOhKMBdjMyUm4AkXvb4ZBgqaPUU1gzJDf5L5BYWCCSStxWhfb4mx%2BygQGZtULiyDl4AyM%2FyaPjHXc%2BjmK40phCKPBasFbObHWqVfysmRDEqZmS1JDYEyKe9EdRgEvbIPaE9Et6WXVQgBUyz0BuJHUdN%2B%2BS%2FMw%2Fa8Slpz013mGS55IHVqUPKgiXJk2okktN8%2BQ8ZAloUDc2kbrfR%2B0EznRHI4WGXiP7pAflya8z2fve%2F6uHhFGFKLXeGfknnz73OkViDkoeynz0Uqv43MgciSD2W6ml5O3x7KDWWZ%2FKM8mdZAzZqZG50q93Bns45jGdq%2BNuyN6PgKV6xloh%2FVsD0rVAeBN0wSK9MZHd1YHUBtitmvZAy3q%2FTrl8MBmJvDItjnrgN8YL2INF56R8ne2ndnfEUzuh2b%2FSEq%2FIn8VpW22T0CxuevqMwtyxLs%2FYbA9do3%2FmX8iHzpETFBluqJQUtFfnvkNh0Z%2F4OTkcnk7zWNrK28zK40Gut%2FYzKlweJgLcVH5xHunOPW8LI1a3PXKtrGpfWG2Ln3FpTz%2B2rus4uhejOlw%2Bjq%2BXuYr8umys0P7%2BDsjXOmKH6TtqfDQfm49oy%2B3XKR7wUeHGFluQPqYBPrMtKff5X4f6qG%2Fm0%2F1rgG2WfPomJjgsjsluxY%2F84l3ohvETj1hcfJ5XyGNq%2FqjFfVLLvfq84Iu1Gz9%2FbGa%2FhipN8TxZ9lUV46LIsPaeWXxfN%2FH2FSy%2FAFrzb%2BWsBAAA"

        VideoRequest.perform(this, url, token) { parsedUrl ->
            runOnUiThread {
                playMedia(parsedUrl)
            }
        }
    }

    private fun playMedia(mediaUrl : String) {
        val dataSourceFactory: DataSource.Factory = OkHttpDataSourceFactory(BaseOkHttpClient.getOkHttpClient(this), "poc")

        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(Uri.parse(mediaUrl))

        // Prepare the player with the source.
        val simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .build().apply {
            playWhenReady = true
            prepare(videoSource)
        }

        playerView.player = simpleExoPlayer

        simpleExoPlayer.addListener(object: Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.e("aaaa", "total time: ${time.elapsedNow()}")
            }
        })
    }
}
