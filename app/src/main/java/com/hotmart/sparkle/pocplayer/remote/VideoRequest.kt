package com.hotmart.sparkle.pocplayer.remote

import android.content.Context
import android.util.Log
import com.hotmart.sparkle.pocplayer.CustomEventListener
import com.google.gson.annotations.SerializedName
import com.hotmart.sparkle.pocplayer.Timer
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import kotlin.time.ExperimentalTime

@ExperimentalTime
object VideoRequest {
    private val eventListener = CustomEventListener(isLogEnabled = false, tag = "OkHttp")
    private val client = OkHttpClient.Builder().eventListener(eventListener).build()

    fun retrofit(token: String, listener: () -> Unit){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.sparkleapp.com.br/rest/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(Api::class.java).getDownload("Bearer $token", "207274")
            .enqueue(object: retrofit2.Callback<ResponseVideo> {
            override fun onFailure(call: retrofit2.Call<ResponseVideo>, t: Throwable) {

            }

            override fun onResponse(
                call: retrofit2.Call<ResponseVideo>,
                response: retrofit2.Response<ResponseVideo>
            ) {
                listener.invoke()
            }
        })
    }

    fun perform(context: Context, url: String, token: String, listener: (String) -> Unit) {
        eventListener.isLogEnabled = true
        eventListener.resetTimer()

        val request = Request.Builder().url("$url?redirect=false")
            .addHeader("Authorization", "Bearer $token")
            .build()

        Timer.mark("start call")
        client.newCall(request).enqueue(object : Callback {
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

interface Api {
    @GET("v2/news/{id}/download")
    fun getDownload(@Header("Authorization") token : String, @Path("id") id: String, @Query("redirect") redirect: Boolean ?= false): retrofit2.Call<ResponseVideo>
}

data class ResponseVideo(
    @SerializedName("data")
    val data : String
)