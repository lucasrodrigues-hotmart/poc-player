package com.hotmart.sparkle.pocplayer.remote

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class BaseOkHttpClient(private val okHttpClient: OkHttpClient) {
    fun get(): OkHttpClient.Builder {
        return okHttpClient.newBuilder()
            .connectTimeout(Timeout.DEFAULT, TimeUnit.SECONDS)
            .readTimeout(Timeout.DEFAULT, TimeUnit.SECONDS)
    }

    companion object {
        private const val CACHE_SIZE = 100 * 1024 * 1024L

        private object Timeout {
            const val DEFAULT = 20L
        }

        fun getOkHttpClient(context: Context): OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(MediaInterceptor(context))
//            addInterceptor(HttpLoggingInterceptor().apply {
//                this.level = HttpLoggingInterceptor.Level.BODY
//            })

            cache(Cache(context.cacheDir, CACHE_SIZE))
        }.build()
    }
}
