package com.prezto.prezto.core.domain.validation

/**
 * Resultado de una validación de dominio. Desacopla la regla de validación de la UI:
 * el ViewModel mapea [errorMessage] al campo correspondiente del State.
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
