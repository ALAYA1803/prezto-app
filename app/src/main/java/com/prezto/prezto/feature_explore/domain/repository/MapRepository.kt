package com.prezto.prezto.feature_explore.domain.repository

import com.prezto.prezto.core.domain.location.GeoLocation
import com.prezto.prezto.feature_explore.domain.model.Item

/**
 * Consulta geoespacial de herramientas. Hoy se resuelve en cliente (Haversine), pero esta
 * interfaz es el seam para migrar a una consulta server-side con PostGIS (ST_DWithin) sin
 * tocar la presentación: solo se reemplaza la implementación.
 */
interface MapRepository {
    suspend fun getItemsWithinRadius(center: GeoLocation, radiusKm: Double): List<Item>
}
