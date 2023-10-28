package com.marneux.marneweather.data

import retrofit2.Response

fun <T> Response<T>.getBodyOrThrowException(): T {
    return body() ?: throw Exception("${code()}: ${message()}")
}