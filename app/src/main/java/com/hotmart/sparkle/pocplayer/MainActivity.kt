package com.hotmart.sparkle.pocplayer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.hotmart.sparkle.pocplayer.remote.BaseOkHttpClient
import com.hotmart.sparkle.pocplayer.remote.VideoRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.time.ClockMark
import kotlin.time.ExperimentalTime
import kotlin.time.MonoClock

@ExperimentalTime
class MainActivity : AppCompatActivity() {
    private lateinit var player: SimpleExoPlayer
    private lateinit var time: ClockMark
    private lateinit var dataSourceFactory: OkHttpDataSourceFactory

    private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
    private lateinit var mp4MediaSource: ProgressiveMediaSource.Factory

    private var mediaType: MediaType = MediaType.MP4
    private var mediaSource: MediaSource = MediaSource.OUT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        time = MonoClock.markNow()
        Log.e("ExoPlayer", "start configuration")
        dataSourceFactory = OkHttpDataSourceFactory(BaseOkHttpClient.getOkHttpClient(this), "poc")
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)

        mp4MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)

        createPlayer()

        val url = "https://api.sparkleapp.com.br/rest/v2/news/207274/download"
//        val url = "https://api.sparkleapp.com.br/rest/v2/news/243410/download"
        val token =
            "H4sIAAAAAAAAAIVTy46rOBT8ohnx7CRLEgiBxnY7GPPYRDw6AQNpEiBgf%2F11ruZKsxhpFkeyzeFUuar8zf26cMsGNX4YCU%2BFjTd697NZHrwPrx0SevB3f39zXy01yhPNrIs4ejeLKtm%2Fyvu5wye4pIn%2FLFxalydrTrXdFPQ%2BRwJzFDsajD0exFikmjNB%2B6bBUDERcZaAYAXZ%2Bx7ZWEsJWNDhDQxZoe87jw2Fd%2Fe7NMZNcPD1su%2BULPQruf4pdDxVCezKRhJkjgIJ4Ii0BlKhWro7XrnRLdL3ddlPY0bGFYpyhqydAXPWgHk6EhKb%2FS4uawVs%2Bv%2B76H9mU1G6R55ra5cm%2B0XOUqGw5IxIBba1yNrgu1%2BXWjf%2B1zekHJU8yV5I3UeJ4odIjGvGuhqymwBSF8AsQ%2BqyZDGeUgZrwBUFapEOhKMBdjMyUm4AkXvb4ZBgqaPUU1gzJDf5L5BYWCCSStxWhfb4mx%2BygQGZtULiyDl4AyM%2FyaPjHXc%2BjmK40phCKPBasFbObHWqVfysmRDEqZmS1JDYEyKe9EdRgEvbIPaE9Et6WXVQgBUyz0BuJHUdN%2B%2BS%2FMw%2Fa8Slpz013mGS55IHVqUPKgiXJk2okktN8%2BQ8ZAloUDc2kbrfR%2B0EznRHI4WGXiP7pAflya8z2fve%2F6uHhFGFKLXeGfknnz73OkViDkoeynz0Uqv43MgciSD2W6ml5O3x7KDWWZ%2FKM8mdZAzZqZG50q93Bns45jGdq%2BNuyN6PgKV6xloh%2FVsD0rVAeBN0wSK9MZHd1YHUBtitmvZAy3q%2FTrl8MBmJvDItjnrgN8YL2INF56R8ne2ndnfEUzuh2b%2FSEq%2FIn8VpW22T0CxuevqMwtyxLs%2FYbA9do3%2FmX8iHzpETFBluqJQUtFfnvkNh0Z%2F4OTkcnk7zWNrK28zK40Gut%2FYzKlweJgLcVH5xHunOPW8LI1a3PXKtrGpfWG2Ln3FpTz%2B2rus4uhejOlw%2Bjq%2BXuYr8umys0P7%2BDsjXOmKH6TtqfDQfm49oy%2B3XKR7wUeHGFluQPqYBPrMtKff5X4f6qG%2Fm0%2F1rgG2WfPomJjgsjsluxY%2F84l3ohvETj1hcfJ5XyGNq%2FqjFfVLLvfq84Iu1Gz9%2FbGa%2FhipN8TxZ9lUV46LIsPaeWXxfN%2FH2FSy%2FAFrzb%2BWsBAAA"

        Log.e("ExoPlayer", "request start: ${time.elapsedNow()}")
        VideoRequest.perform(this, url, token) { parsedUrl ->
            Log.e("ExoPlayer", "response end: ${time.elapsedNow()}")
            runOnUiThread {
                playMedia(parsedUrl)
            }
        }
    }

    private fun createPlayer() {
        val loadControlStartBufferMs = 1500
        val loadControlBufferMs = 60000
        val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
            loadControlBufferMs,
            loadControlBufferMs,
            loadControlStartBufferMs,
            DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
        ).createDefaultLoadControl()

        player = SimpleExoPlayer.Builder(this)
            .setLoadControl(loadControl)
            .build().apply {
                playWhenReady = true
                playerView.player = this
                addListener(object : Player.EventListener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        Log.e("ExoPlayer", "playing changed time: ${time.elapsedNow()}")
                    }
                })
            }
    }

    private fun playMedia(mediaUrl: String) {
        when {
            mediaType == MediaType.MP4 && mediaSource == MediaSource.IN -> configureMP4(mediaUrl)
            mediaType == MediaType.MP4 && mediaSource == MediaSource.OUT -> configureMP4("https://v16.muscdn.com/05cb1c36bc0c6d2299ae7aa850916bfd/5e696bb7/video/tos/useast2a/tos-useast2a-ve-0068c002/512942de4f8a4cc497ec066b3b3046df/?a=1233.mp4")
            mediaType == MediaType.M3U8 && mediaSource == MediaSource.IN -> configureHLS(mediaUrl)
            mediaType == MediaType.M3U8 && mediaSource == MediaSource.OUT -> configureHLS("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        }
    }

    private fun configureHLS(mediaUrl: String) {
        player.prepare(hlsMediaSourceFactory.createMediaSource(Uri.parse(mediaUrl)))
    }

    private fun configureMP4(mediaUrl: String) {
        player.prepare(mp4MediaSource.createMediaSource(Uri.parse(mediaUrl)))
    }

    enum class MediaType {
        MP4, M3U8
    }

    enum class MediaSource {
        IN, OUT
    }
}
