package com.hotmart.sparkle.pocplayer

import android.net.Uri
import android.os.Bundle
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
            "H4sIAAAAAAAAAG1SyY6rOBT9ojwZHEKyZEzgYbsIZvImChACBjJUBsBf305JJXVLvbiSp3PvGXya%2FabYli1p%2FSgWnoJb7%2BFd9lppeSuvu2WJ5W%2F%2BnGZfKdVkzlStKdL481hUmfkuL%2Fs%2B3OExz%2FzvYps05c545ermGQz%2BTIQ7IN4BZHsgoB3AHD2x3TcsUlo8xHNAzwqzPY1tcwUJA6L5MxjzApq9x2%2BFd%2FH7PA3bwPJhOfSARX4l19cChs8qw33ZSoLcAZiimdBuSRSslNvNXG3jcwzNphyeD0YfExblC%2FPuhbgzBdyDRDhjwH9qDnioEfiLS4zyF5e6Y2lpvFDBK794egmTsYA%2BYNl%2FtOqIxhDZzoxpPCLbkPW0ysEVx3T8n7uHHmb7WzWs9djd12Hv6R%2BPSOqoxEaT9GhG9ll6ZPbMUlqkOiKgIWS0FIjmM7GbHguph5YjojIou9OInUNkLRViS6yQs2ykYR5DQmNAxA%2B%2FpTxbEjsWhOZCclDCvrqFaWXFvbnPoZuwGEBsuy8iexJaZQz6u6IDE%2BN9g%2FlZoAhoiBtLyWVkafjMOW7QDABWpT7hqIifl5Kj1PL41IR5%2FrvWvXZsWdaMHr9K3meV0PMo89Jkjk2V7WWW%2Fq3a9n05e6t09mnsuFEEJjdTErpPvE%2F2MgNllJ52BfzZ%2F%2BsN9vdAS2Lr828SwTK%2Fllm1WTS2x8v%2BJme2%2BYCkplwWeiLutnkEABo%2BvnYT3kotwgCEuj2z40lqGz98c3W6sVQDmdp3nx6YV4PEy%2FycJ0lDgVoAJO7TYymzk32NkdCkzQUb8sHl3gX8YaZPj61pOSlyg41mv084vwfmHN1AM9X9xd%2F4xUkPVvfVGLcPOF9RajLXrjWv2xhvWl5qqF6V6dtkYc9Ef3iys6NVw%2BHw3oUq27b0OzStqbxMi8f6Uanp9%2FrSqdgaq2quFkJfr9RIxPfAWVb60LoOKI5ff%2Bd%2BuFt6VUc2XAfdARrRZj%2BpyFjHO33gNhvf1XHFFu9C6Q7hLlo5enLkMRhm1ijjagMqAw87b3XqF%2BOOvmvrQXblpjjGbl4XXc6blUnTTLFyZW%2B9LLjZfq28ejlmLIuMe%2FY3zAthPoLXfbkIqrAeusa%2BekqtHpdPQb4m3Lz1XUa1l28VBG7MNfLPBw9%2BjcNVOVlXXNvKy%2FgHMSFwa6sEAAA%3D\",\"refresh_token\":\"H4sIAAAAAAAAAG1SWY%2BjOBD%2BRRkZCDkeORMTbAdiDvOymkAGMEfTHRLAv35MSy3tSvtQkjGuqu96LF51P%2BU1qb1bJKCCa%2FiEfajnFtzBZkhjyzv%2Beiyekqvxkqp6dU%2Bi9bEoUvOd92EbnPHEUu%2Frfoqr%2FGy8mHoc%2Fc5biHA7xBuAbAh82gDM0YjttspuSo27aPFpqWQ21LMTU5AwNLSsizG%2Fa2YL%2BXCHvdeyJKh9y9PyrgXZzSvkuSpOzbq%2FZiLSMY90tIAFJV7l02BhFI2EOiCzFI7sQEdJ3GDqtpltrHM%2B7lowFilu81qS4w7AFC2ENluiYCU%2FHZfiFJWRZlZ5Nz4z%2BpyxyF%2BYNy%2FEndnnUCPCmXz%2BXYvPA51oP32xkf%2F0Je6UWzq%2Fq%2BDFerjPtXi6ax7I0v%2FotEc00pDtLJhGE7INWaOVd674nUz%2F8%2B%2B5D9JwKLrDPnLDP0EL96u%2BJHFUYqNZ6rsgu5T6mq3kXiPVEVIPLaO5QJQtxK5aLCQfmk%2BISpPtRic205C1VYgte4XcZaNVT43QCBDxjW8r77bEjgShTEgMStAWQ5AUVtSaIdPcOIuAhm33ReRMQos007zzvQFzxtsK81KgG9ARN7YSy5Qlwcg4rqRfAKuSn3BUxMutxCi5PNeaMWc%2F5z2spzpLqwnyj5mseClb%2FdK%2FM5CG0ktvKE5tmy9wlywejRz3dgOzmyoxDWO4%2Bi09UCapaXPXvr%2F%2F9QZ7IdDjyFozF4ss9f5Ir%2Br0NtW%2F%2B3CQO2tGcwV1uJaajYjjmlgSN4Wan0BAkjVnuCI0ABktARKBWPEydR6yRAep2jbrDMyLjlAk%2FXNGkgQC1QAwYUhvZOYSR841JkJjmeWsY53LYQ9%2BZa5R1gHbKYW5R%2BEtegXu3PYpdOCnU%2BV1V%2B6w%2Fex3mrWfsn5vs4NSO9Vw7w0ssrBctHJ4GodBuVTBbqCq%2FTry6yjy4fAcFpV9dTvwqWzirv1Djh%2FvufcZNCeZtGqenRNpx%2FjQXwvva3SrzVPvPx7NnmzxMzTLx9TRzb4%2BmDdn3JQfld89%2FOpwy19e6ti4uRC0SXpxxddWD95upE1nHLzN64B9CM7X3atuHYguojq%2BNX%2FX82Od9vEXQ1ytMfm0YqbTM6ytfjZLZvroovyjzcxzXa26HjcNvnxunPgy4W2XP5pt2A%2BoARu%2FODoi7Hdx6aDmpKHrR67wuzfjzqge2%2FdhNJdqr77DarcYfwEtaDyG5wQAAA%3D%3D"

        when(requestType){
            RequestType.WARMED -> requestWarmed()
            RequestType.OKHTTP -> requestOkHttp()
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
