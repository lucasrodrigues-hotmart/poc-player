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
    private lateinit var timer: Timer
    private lateinit var dataSourceFactory: OkHttpDataSourceFactory

    private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
    private lateinit var mp4MediaSource: ProgressiveMediaSource.Factory

    private var mediaType: MediaType = MediaType.MP4
    private var mediaSource: MediaSource = MediaSource.OUT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timer.mark("start configuration")
        dataSourceFactory = OkHttpDataSourceFactory(BaseOkHttpClient.getOkHttpClient(this), "poc")
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)

        mp4MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)

        createPlayer()

        val url = "https://api.sparkleapp.com.br/rest/v2/news/207274/download"
//        val url = "https://api.sparkleapp.com.br/rest/v2/news/243410/download"
        val token =
            "H4sIAAAAAAAAAIVTXZOqOBD9RbMVvvT6iIIarklEEiC8WAoOEECZAYXk12%2Bc2lu1D1u1D13V3en06fQ5ucmguu7ymtRBxBQ0cA0HeD85%2BQYuYNOn8SZY%2FXWTgZGbsUxNp7om7F2sinT9yu%2BnNtzjiafB93UXV%2FnefXJzNR66QBLFTJIwgJNTe6BcIlWOeIcsHBkVT%2BLuQBFAtDS4Ki3s4Sb7Acbiaq1bKPorvActT8L6sAmsvGtBFgWF9h9XKxyLFLd5rQcUPsAUSUIbmxjYyHcrWexYyax1lXfjkNFhxip%2FYtE8kfDng4AWUf50ED%2BmY99B3vj%2Fb7H%2B9I5VvtvKizm3PF1PupeBlTsjwQzkuZO2ZXgPqtxsh%2F86I2ALLmn2IsaapSCIiBrmTLQVFqVCEXCQcO0DDacsCUcucIUkANhkFlK%2BiURpZzRfIqpjz5eYhg6hpakxnpiW%2Bi7SWKEilGvcxsDe8DMf8XTOy2fkQe2zJWZBemHbe9gGIUvwHCcxxiqcr6LRPRsrNgt5Mh2MEu5wym2NPRIKJ7IBAO3i5pBAxSkasVe0WKEZC2iTHdN7HZZvQ5Sbf3wiNaddbL%2FFpOe2NQ8Wof6E6qnmaQwueqeX9NRnKapJO9TMWK9ZM6JTvIoZiCP4rtMc5PugynTtO%2F5XDY1YQeLYfWvkH30GErZAY%2FbgEml9dHr3wreRxy2twVnrUWsQKlQDG4lTdaBVrXcoOV0LpFyFo%2Fe8eLgk8bPYrvrs%2FQkEtzLRKM3ffKBtgxR863jS3DjEa3UPqLltDN4hM%2BuCikv9YS74xjf5fXazRy08a9zc5Gy%2FAmFGBArEv36%2FpPEbP4bv5bm1rNtXP7wiUqh90Cz69lcJ%2B%2B8qhZ9MdKuhOw5Gn03ZOZSbvhZukeXFcagJS8%2FLD9wttw9%2FBqd1VD2r%2Fi7PH3GTRttj%2B%2FRi8GVfy4V3vzvwszEx6%2FaJ74DxMJjbRrJf3fmrr9hkqRNPFzZsX88x6tfuQJPvX%2FXlaV%2FTvrejyr1NgDsPjmUe3aa71AlXbpbW9tEnvjGA88NqN%2FRUP7zN9yL17G4fHa%2Fpi%2FFLURtg%2FfTN48P%2B9JD1nZdZWBFnczwPjpfcb2RVc2V%2BOCvfsKbjuN86gQ2nONkPsx9cvP5Yh%2BfjIieh14Jy2aTu3w6Y9rGsBAAA\",\"refresh_token\":\"H4sIAAAAAAAAAIVT2Y6rOBD9ooyMgUvyGJYQ09iEYBbzEhHTHdYktyEs%2FvpxWnOleRhpHkqqssvnuKpOfa5edXV5HdReFAukkBoN6H7WuYV%2BofaZJZa3%2B%2Btz9RQOkzWDenVN43eyKDNz4vdzFx7JzDLv%2B%2BomFT%2FuXwzuRr%2F31kDEMEhjQNJz51O2YnEbiYtVEikVS5PepxhgelOYuKnEJm3%2BQ0yaq2p2qHle0d3rWBrWvuWpvO9AHnml9KvSbd%2F8HaFoCew9JCuAmHaS41xjgcbARoBFQGNNq%2BHemZnAMxbxG%2BdxVcOxzEjHa1lc4wBC8RrQVgsUonB3t5ZufItVs%2BL9OOR0WIjgL9K0L9w4i98gNRDO7Dc%2FJmNHx%2Fb4%2F31Q%2F2AngruHtYBLxzJzllgKEfsFN7GC7f0szQjvXsVhN%2FzXXQAOoMjyKVDMOANeFIhhyZuuIs1N4AjouNlrPg3nPA1H1pAKrwAQGKtYOBA3Ny2n3MBUxrazEhrqAb1ByfEi9CbfYskVioAyydsqxB5%2B%2FhfY8szmC7aR9GODxF5WxId72HlhnJIlSRNCRLhcm1ZitmoCy%2FUMdYJTpjPKNMk9BhTNgQUAdpPWT5FgFI%2FELjsi8EIapAVuLPs6GG%2FDlME%2FfrBKPfSJ9hai1In8bwsl3ozruWZZAgrZ0yI7P%2FMM10E31LFimnE74nOyS2KQROidJ2fAj16Vy9x3%2FK8cGsVlkCT7ty7%2B0ba3og5Izicoorc%2BzI5Apsv6dD%2FFsj4k9XtosQU0qbvZp47G6H4httfkjVn9LE5PhiJNXuVh98zfC9QwNW9aIee3%2BLRr3%2FqUOzDL2eiB3VW%2B7A22W4X1GOa9V7FVLlsclgpiUMk9RWs%2BaCH4h%2F95dNDFSR%2FPU51NANnFFH%2BfuDvY5NJYxXO%2F%2Byx3m%2BOXeFXulBz4yRVb1ny8ErApnuA3d0u1LWr%2F3C6%2FZ802Zu2BzGx7c8bNRBgPpnIDDfiNx2w6lIeAf7%2F4V%2BiZ2uO237ZU46gyQ6iaw51pJ4c%2BMu%2F6%2BqVX%2BuKo2rxMwLVCP9lZ9eFk%2FuqU1iSX0AFQC7EV%2BetlZ9xxATh7frTsBTXjdBTr8hyqVxuOx5Sgj8x4%2BnZltNb0dWFwKcaz%2Fn22FtO%2FlOQynqzevWxsdLxFcub1BPsEfe2TYbvBm%2FEu3MN0f2ipH23dLbgSw9jdl49kqLpHSPN%2BMQqYjvDc5vu%2FAVp7po7oBAAA"

        Timer.mark("request start")
        VideoRequest.perform(this, url, token) { parsedUrl ->
            Timer.mark("response end")
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
                        Timer.mark("playing changed time")
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
