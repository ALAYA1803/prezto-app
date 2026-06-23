package com.prezto.prezto.core.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.prezto.prezto.core.domain.location.GeoLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class FusedLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): GeoLocation? {
        if (!hasLocationPermission()) return null
        return try {
            suspendCancellableCoroutine { continuation ->
                client.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(
                            location?.let { GeoLocation(it.latitude, it.longitude) }
                        )
                    }
                    .addOnFailureListener { continuation.resume(null) }
            }
        } catch (e: SecurityException) {
            null
        }
    }

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
}
