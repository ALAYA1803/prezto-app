package com.prezto.prezto.core.data.network

import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Traduce excepciones técnicas a mensajes claros para el usuario. Centraliza el copy de errores
 * para mantener un tono consistente y profesional en toda la app.
 */
class ErrorMapper @Inject constructor() {

    fun map(throwable: Throwable): String = when (throwable) {
        is UnknownHostException, is ConnectException ->
            "Sin conexión a internet. Revisa tu red e inténtalo de nuevo."

        is SocketTimeoutException ->
            "La conexión tardó demasiado. Inténtalo nuevamente."

        is HttpException -> mapHttpCode(throwable.code())

        is IOException ->
            "Tuvimos un problema de red. Inténtalo de nuevo."

        else ->
            "Ocurrió un error inesperado. Estamos trabajando en ello."
    }

    private fun mapHttpCode(code: Int): String = when (code) {
        401, 403 -> "Tu sesión expiró. Inicia sesión nuevamente."
        404 -> "No encontramos lo que buscabas."
        408 -> "La solicitud tardó demasiado. Inténtalo de nuevo."
        429 -> "Demasiados intentos. Espera un momento antes de reintentar."
        in 500..599 -> "Servidor en mantenimiento. Vuelve a intentarlo en unos minutos."
        else -> "No pudimos completar la operación. Inténtalo de nuevo."
    }
}
