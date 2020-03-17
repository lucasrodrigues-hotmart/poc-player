package com.hotmart.sparkle.pocplayer.remote

import android.content.Context
import android.util.Log
import com.hotmart.sparkle.pocplayer.CustomEventListener
import com.hotmart.sparkle.pocplayer.Timer
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
object VideoRequest {
    fun perform(context: Context, url: String, token: String, listener: (String) -> Unit) {
        val request = Request.Builder().url("$url?redirect=false")
            .addHeader("Authorization", "Bearer $token")
            .build()

        Timer.mark("start call")
        OkHttpClient.Builder().eventListener(CustomEventListener()).build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    Timer.mark("response success")
                    val obj = JSONObject(response.body?.string().toString())
                    Timer.mark("parse body success")

                    val headers = response.headers.values("set-cookie")
                    Timer.mark("parse headers success")
                    val cookies = if (headers.size >= 3) {
                        headers[0] + ";" + headers[1] + ";" + headers[2]
                    } else {
                        ""
                    }
                    Timer.mark("cookies success")

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