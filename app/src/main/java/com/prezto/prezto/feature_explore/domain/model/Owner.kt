package com.prezto.prezto.feature_explore.domain.model

import com.prezto.prezto.core.domain.model.TrustScore

/**
 * Value Object con los datos del propietario relevantes para el flujo de descubrimiento.
 * No es la entidad de usuario completa (esa vive en feature_profile); aquí solo viaja
 * lo que el arrendatario necesita para confiar y decidir.
 */
data class Owner(
    val id: String,
    val name: String,
    val trustScore: TrustScore,
    val isVerified: Boolean
) {
    /** Inicial para avatares generados (placeholder premium sin imagen). */
    val initial: String
        get() = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
}
