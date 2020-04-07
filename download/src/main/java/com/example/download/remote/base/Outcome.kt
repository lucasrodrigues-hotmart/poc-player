package com.example.download.remote.base

sealed class Outcome<out T> {
    data class Success<out T>(val value: T) : Outcome<T>()
    data class Failure(val throwable: Throwable) : Outcome<Nothing>()
}
