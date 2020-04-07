package com.hotmart.sparkle.pocplayer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.download.DownloadManager
import com.example.download.DownloadManagerBuilder
import com.example.download.remote.RestClient
import com.example.download.remote.repository.PlaylistDataRepository
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.hotmart.sparkle.pocplayer.remote.BaseOkHttpClient
import com.hotmart.sparkle.pocplayer.remote.VideoRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.HttpURLConnection
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainActivity : AppCompatActivity() {
    private lateinit var player: SimpleExoPlayer
    private lateinit var dataSourceFactory: OkHttpDataSourceFactory

    private lateinit var hlsMediaSourceFactory: HlsMediaSource.Factory
    private lateinit var mp4MediaSource: ProgressiveMediaSource.Factory

    private var mediaType: MediaType = MediaType.M3U8
    private var mediaSource: MediaSource = MediaSource.IN
    private var requestType : RequestType = RequestType.WARMED

    private lateinit var url : String
    private lateinit var token : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Timer.mark("start configuration")
        dataSourceFactory = OkHttpDataSourceFactory(BaseOkHttpClient.getOkHttpClient(this), "poc")
        hlsMediaSourceFactory = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)

        mp4MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)

        createPlayer()

//        url = "https://api.sparkleapp.com.br/rest/v2/news/207274/download"
//        url = "https://api.sparkleapp.com.br/rest/v2/news/243410/download"
//        url = "https://api.sparkleapp.com.br/rest/v2/news/256058/download"
        url = "https://api.sparkleapp.com.br/rest/v2/news/254036/download"

        token  =
            "H4sIAAAAAAAAAJ1TyZKjOBT8ouoAYYryEQzGYCRZIATi0lHgskECr5jt6xu7YzpqJmYucyBE6G2Zeplfo1%2FmblHhyo%2FiyVNR5d29U6gXK%2B%2Fdk5eUrfzlj6%2FRVwvAxhToZZ7Ez%2BTxM0Fl0YRd0Kh17g4dcctu76plcSIPDpYtno4L3oQyawgIElShCbbIjXUYKXomoBLQQkMNk3DiOraRRNFzMBK5ZtWeuOTeya95Qqpg5WtFUytZ5O%2Fn%2F3OukXaforqoZoDCGeEU94geAVb9CwfrMXM%2FjjRh99z9MOB8HwhPwavFgCbvMecZUJh9IByAo8UAVwuAtT91j%2FxVtzQ%2F3aWSJ%2Btxv9JFDhRjni946ssc%2FOb6J27fB0RnTpM3Yso1aJs9VvyRp%2BiS%2F1tsaq29W4N8aglbL1exaFmsEAPaUsMJVDPhtJwWAI2KghJnfqNaIlu2CDCRUXPIGmfioljgyVERlRMUno5EMfN%2FcoonNJkACaJC2wNQwB7Se%2F%2FMxTYfkZA9sk0d0cLgIOtYXO44u2ShXKufdVnFUzh9puH1Ky2t0EE3pNT9%2F8GFbXPCdqFDO57rTSNrMskTVEPXa7nwKxQpSpaEIkh8ASl59qgRgDMPqfJJ9hhkZb5BNZ7uBpyc8Xm%2BvnHWR8MWT2FCGi%2BwfQSQmgpc9RVPmTLvZJzxX7IUVri%2BV7FqWbFsYciW8xuzyKv673cWXVse%2BRabNXvnid8VzVrmmvfUHcgSXc5Yu%2BJlCmvMQaju5zn%2F6JWSeJjP8BBKZrE1i8KnlhvU5ak18r%2Fqa0QI8wPqsEM85xCltObZf6%2F5lsOcck3j%2F44TyeLv%2BF%2BeqJdVUb18ct278ulTiRMya4XoWaRokEoQ0FDMe215AxeoUkbYOD2mWQ0pVDl9%2BU3kbl3np%2FDwOfsPVx5AgM86NvuXd204%2Fu4BW0yPOo%2FmHk9dzftETVbxBFYH8mMdM%2Bvi04Ytixu3o%2B2oIs8nJ7Rx23Zk9y9c9WwydEdob7Hmk315EL01aTo8n3v2Fhol%2Fow26ul2atbT4y1KwyT2dost9fuuvuWai9%2F8%2B%2BlqrYf03GKbDcnufeySNxSAROzsfrhdXeXS6ot9xrTYPh%2FMPO3tG06HbUg8vgFSgWO3WRERHiNyz633rWu35zL4%2BvB8x9%2F2hj3cvESNt1A6eWs3td8Nd2R3aCfeW%2FklV48pruXo19nPw44x%2FRoFiUD5BC902rOPxxDxwLkuap3mAekzh2ULw%2F6w1jeze9%2B4hnnc2NRb7jynWe3IKVPDq%2F3e6%2FTRkm1wV34C60Pf%2B%2Bch2XsEsDLM3jaegY%2B%2FAAyg15quBQAA"

        playRemoteBtn.setOnClickListener {
            when(requestType){
                RequestType.WARMED -> requestWarmed()
                RequestType.OKHTTP -> requestOkHttp()
            }
        }
        downloadBtn.setOnClickListener {
            download()
        }
        playLocalBtn.setOnClickListener {
            configureLocal(url)
        }

        DownloadManager.addListener { percent ->
            downloadStatus.text = "Downloaded: $percent%"
        }
    }

    private fun download() {
        Log.e("aaaa", "starting download")

        val mediaCode = "ne3gr1"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val playlist = PlaylistDataRepository(RestClient().getPlaylistApi())
                    .getPlaylist(mediaCode, "Bearer $token", "desafio30diasbr")

                DownloadManager.download(this@MainActivity, mediaCode, playlist.manifest?.url ?: "")
            } catch (e: Exception) {
                if (e is HttpException) {
                    when (e.code()) {
                        HttpURLConnection.HTTP_UNAUTHORIZED ->
                            Log.e("aaaa", "401 error")
                        HttpURLConnection.HTTP_FORBIDDEN ->
                            Log.e("aaaa", "403 error")
                        else ->
                            Log.e("aaaa", "another error")
                    }
                }
            }
        }
    }

    private fun requestWarmed() {
        Thread {
            VideoRequest.retrofit(token) {
                Timer.mark("request start")
                requestOkHttp()
            }
        }.start()
    }

    private fun requestOkHttp() {
        VideoRequest.perform(this, url, token) { parsedUrl ->
            Timer.mark("response end")
            BaseOkHttpClient.eventListener.resetTimer()
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

    private fun configureLocal(mediaUrl: String) {
        val dataSourceFactory = CacheDataSourceFactory(
            DownloadManagerBuilder.getDownloadCache(this), DefaultDataSourceFactory(this, "PocPlayer")
        )

        player.prepare(HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(mediaUrl)))
    }

    enum class MediaType {
        MP4, M3U8
    }

    enum class MediaSource {
        IN, OUT
    }

    enum class RequestType {
        OKHTTP, WARMED
    }
}
