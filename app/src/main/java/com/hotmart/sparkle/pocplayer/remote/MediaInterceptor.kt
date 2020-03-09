package com.hotmart.sparkle.pocplayer.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MediaInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return chain.proceed(addHlsHeader(response))
    }

    private fun addHlsHeader(response: Response): Request {
        val prefs = context.getSharedPreferences("preferences_media", Context.MODE_PRIVATE)

        return Request.Builder()
            .url(response.request.url)
            .addHeader(REFERER, "https://api.sparkleapp.com.br/")
            .addHeader(COOKIE, prefs.getString(COOKIE, EMPTY) ?: EMPTY)
            .build()
    }

    companion object {
        private const val COOKIE = "Cookie"
        private const val REFERER = "Referer"
        private const val EMPTY = ""
    }
}
