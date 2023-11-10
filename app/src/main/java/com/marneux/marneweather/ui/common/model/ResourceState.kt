package com.marneux.marneweather.ui.common.model

sealed class ResourceState<out T> {
    object Idle : ResourceState<Nothing>()
    object Loading : ResourceState<Nothing>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error(val exception: Throwable) : ResourceState<Nothing>()
}

fun <T> Result<T>.toResourceState(): ResourceState<T> =
    fold(
        onSuccess = { ResourceState.Success(it) },
        onFailure = { ResourceState.Error(it) }
    )