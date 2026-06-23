package com.prezto.prezto.core.data.location

import com.prezto.prezto.core.domain.location.GeoLocation

/**
 * Abstrae el acceso a la ubicación del dispositivo. Devuelve null si no hay permiso o no
 * se pudo obtener; así el resto de la app no depende de Google Play Services directamente.
 */
interface LocationProvider {
    suspend fun getCurrentLocation(): GeoLocation?
}
