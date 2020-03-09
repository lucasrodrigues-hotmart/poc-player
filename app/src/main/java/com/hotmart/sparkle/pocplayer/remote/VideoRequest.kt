package com.hotmart.sparkle.pocplayer.remote

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object VideoRequest {
    fun perform(context: Context, url: String, token: String, listener: (String) -> Unit) {
        val request = Request.Builder().url("$url?redirect=false")
            .addHeader("Authorization", "Bearer $token")
            .build()

        OkHttpClient.Builder().build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val obj = JSONObject(response.body?.string().toString())

                    val headers = response.headers.values("set-cookie")
                    val cookies = if (headers.size >= 3) {
                        headers[0] + ";" + headers[1] + ";" + headers[2]
                    } else {
                        ""
                    }

                    val prefs = context.getSharedPreferences(
                        "preferences_media",
                        Context.MODE_PRIVATE
                    )
                    prefs.edit().putString("Cookie", cookies).apply()

                    listener.invoke(obj.getString("data"))
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("aaaa", "content player failure")
                }
            }
        })
    }
}