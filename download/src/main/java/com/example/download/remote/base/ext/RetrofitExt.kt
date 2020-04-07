package com.example.download.remote.base.ext

import com.example.download.remote.base.Outcome
import retrofit2.HttpException
import retrofit2.Response

fun <R : Any> Response<R>.parseResponse(): Outcome<R> {
    if (isSuccessful) {
        val body = body()

        if (body != null) {
            return Outcome.Success(body)
        }
    }

    return Outcome.Failure(HttpException(this))
}
