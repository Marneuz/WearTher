package com.marneux.marneweather.data

import kotlinx.coroutines.CancellationException
import retrofit2.Response

/** Función de extensión para la clase Response de Retrofit.
Extrae el cuerpo de la respuesta o lanza una excepción si el cuerpo es nulo.*/
fun <T> Response<T>.getBodyOrThrowException(): T =
    body() ?: throw Exception("${code()}: " + message())

/** Maneja las excepciones específicas de los repositorios.
Propaga las excepciones de cancelación y convierte otras excepciones en Result.failure.*/
fun Exception.handleRepositoryException(): Result<Nothing> {
    if (this is CancellationException) throw this
    return Result.failure(this)
}

/** Función segura para llamar a operaciones que pueden lanzar excepciones.
Devuelve un Result exitoso si la acción se completa sin excepciones,
o un Result fallido si ocurre una excepción.*/
inline fun <T> safeCall(action: () -> T): Result<T> {
    return try {
        Result.success(action())
    } catch (e: Exception) {
        e.handleRepositoryException()
    }
}