package com.example.download.remote

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class BaseOkHttpClient(
    private val followRedirects: Boolean,
    private val fromIncentive: Boolean,
    private val okHttpClient: OkHttpClient
) {
    fun get(): OkHttpClient.Builder {

        val timeout = if (fromIncentive) Timeout.INCENTIVE else Timeout.DEFAULT

        return okHttpClient.newBuilder()
            .followRedirects(followRedirects)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
    }

    companion object {
        private const val CACHE_SIZE = 100 * 1024 * 1024L

        private object Timeout {
            const val DEFAULT = 20L
            const val INCENTIVE = 40L
        }

        fun getOkHttpClient(context: Context): OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(AuthInterceptor())
            addInterceptor(HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            })
            cache(Cache(context.cacheDir, CACHE_SIZE))
        }.build()
    }
}
