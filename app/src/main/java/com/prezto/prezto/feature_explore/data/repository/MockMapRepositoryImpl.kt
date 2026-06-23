package com.prezto.prezto.feature_explore.data.repository

import com.prezto.prezto.core.domain.location.GeoLocation
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.repository.ExploreRepository
import com.prezto.prezto.feature_explore.domain.repository.MapRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Implementación mock: filtra en cliente con Haversine sobre los ítems del ExploreRepository.
 * El día de PostGIS, esta clase se reemplaza por una que llame al endpoint geoespacial.
 */
class MockMapRepositoryImpl @Inject constructor(
    private val exploreRepository: ExploreRepository
) : MapRepository {

    override suspend fun getItemsWithinRadius(center: GeoLocation, radiusKm: Double): List<Item> {
        val items = exploreRepository.getFeaturedItems().first()
        return items.filter { item ->
            center.distanceKmTo(GeoLocation(item.latitude, item.longitude)) <= radiusKm
        }
    }
}
