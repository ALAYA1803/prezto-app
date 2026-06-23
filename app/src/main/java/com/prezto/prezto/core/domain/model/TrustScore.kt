package com.prezto.prezto.core.domain.model

import java.util.Locale

/**
 * Shared Kernel (DDD): el TrustScore es un concepto transversal de confianza/gamificación
 * usado por varios features (explore, profile, rental), por eso vive en core y no en un feature.
 *
 * Value Object inmutable y autovalidado: SIEMPRE en el rango [0.0, 5.0].
 */
@JvmInline
value class TrustScore(val value: Double) {

    init {
        require(value in 0.0..5.0) {
            "TrustScore inválido: $value. Debe estar entre 0.0 y 5.0."
        }
    }

    /** Representación lista para UI, ej. "4.9". */
    val formatted: String
        get() = "%.1f".format(Locale.US, value)

    /** Nivel de confianza derivado; alimenta badges y la Garantía Dinámica. */
    val level: TrustLevel
        get() = when {
            value >= 4.8 -> TrustLevel.ELITE
            value >= 4.0 -> TrustLevel.TRUSTED
            value >= 3.0 -> TrustLevel.STANDARD
            else -> TrustLevel.NEW
        }
}

enum class TrustLevel(val label: String) {
    ELITE("Élite"),
    TRUSTED("Confiable"),
    STANDARD("Estándar"),
    NEW("Nuevo")
}
