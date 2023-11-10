package com.marneux.marneweather.data

import kotlinx.coroutines.CancellationException
import retrofit2.Response

fun <T> Response<T>.getBodyOrThrowException(): T =
    body() ?: throw Exception("${code()}: " + message())

fun Exception.handleRepositoryException(): Result<Nothing> {
    if (this is CancellationException) throw this
    return Result.failure(this)
}

inline fun <T> safeCall(action: () -> T): Result<T> {
    return try {
        Result.success(action())
    } catch (e: Exception) {
        e.handleRepositoryException()
    }


}