package com.prezto.prezto.core.domain.util

/**
 * Contrato estándar con el que la capa de datos comunica el resultado de una operación
 * a los ViewModels. Evita propagar excepciones técnicas hacia la presentación.
 */
sealed interface NetworkResult<out T> {

    data class Success<T>(val data: T) : NetworkResult<T>

    /** Error ya mapeado a un mensaje amigable; conserva la causa para logging/observabilidad. */
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : NetworkResult<Nothing>

    data object Loading : NetworkResult<Nothing>
}

/** Ejecuta [block] solo si el resultado fue exitoso. */
inline fun <T> NetworkResult<T>.onSuccess(block: (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) block(data)
    return this
}

/** Ejecuta [block] solo si el resultado fue un error. */
inline fun <T> NetworkResult<T>.onError(block: (String, Throwable?) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) block(message, cause)
    return this
}
