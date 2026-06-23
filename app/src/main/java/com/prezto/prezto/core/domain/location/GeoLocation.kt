package com.prezto.prezto.core.domain.location

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Value Object de coordenadas (shared kernel). Incluye el cálculo de distancia por la
 * fórmula de Haversine: barato (solo trigonometría, sin asignaciones), ideal para filtrar
 * listas de forma reactiva mientras el usuario mueve el slider de radio.
 */
data class GeoLocation(
    val latitude: Double,
    val longitude: Double
) {
    /** Distancia en kilómetros entre este punto y [other]. */
    fun distanceKmTo(other: GeoLocation): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(other.latitude - latitude)
        val dLon = Math.toRadians(other.longitude - longitude)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(dLon / 2).pow(2)
        return earthRadiusKm * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}
