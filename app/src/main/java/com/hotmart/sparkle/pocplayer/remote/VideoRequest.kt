package com.hotmart.sparkle.pocplayer.remote

import android.content.Context
import android.util.Log
import com.google.gson.Gson
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


    fun retrofit( url: String, token: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.sparkleapp.com.br/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(Api::class.java)

        val result = service.getDownload("Bearer $token","207274" ).execute()
    }


    fun perform(context: Context, url: String, token: String, listener: (String) -> Unit) {
        val request = Request.Builder().url("$url?redirect=false")
            .addHeader("Authorization", "Bearer $token")
            .build()

        Timer.mark("start call")
        OkHttpClient.Builder().build().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                try {

                    Timer.mark("reponse sucess")
                    val obj = JSONObject(response.body?.string().toString())
                    Timer.mark("parse body sucess")

                    val headers = response.headers.values("set-cookie")
                    Timer.mark("parse headers sucess")
                    val cookies = if (headers.size >= 3) {
                        headers[0] + ";" + headers[1] + ";" + headers[2]
                    } else {
                        ""
                    }
                    Timer.mark("cookies sucess")

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
    @GET("v2/news/{id}}/download")
    fun getDownload(@Header("Authorization") token : String, @Path("id") id: String, @Query("redirect") redirect: Boolean ?= false): retrofit2.Call<ResponseVideo>
}

data class ResponseVideo(
    @SerializedName("data")
    val data : String
)