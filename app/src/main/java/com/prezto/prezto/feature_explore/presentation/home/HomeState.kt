package com.prezto.prezto.feature_explore.presentation.home

import com.prezto.prezto.core.domain.location.GeoLocation
import com.prezto.prezto.feature_explore.domain.model.Category
import com.prezto.prezto.feature_explore.domain.model.Item

data class HomeState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val allItems: List<Item> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val userLocation: GeoLocation? = null,
    /** Distancias precalculadas (itemId -> km). Se calcula una vez, no por frame. */
    val itemDistances: Map<String, Double> = emptyMap(),
    val searchRadiusKm: Float = DEFAULT_RADIUS_KM,
    val locationPermissionDenied: Boolean = false,
    val error: String? = null
) {
    /**
     * Lista visible derivada en tiempo real del query + categoría + radio de cercanía.
     * El filtro por distancia usa las distancias YA precalculadas (sin trigonometría aquí).
     */
    val visibleItems: List<Item>
        get() = allItems.filter { item ->
            val matchesCategory = selectedCategoryId == null || item.categoryId == selectedCategoryId
            val matchesQuery = searchQuery.isBlank() ||
                    item.title.contains(searchQuery.trim(), ignoreCase = true)
            val matchesRadius = if (userLocation == null) {
                true
            } else {
                val distance = itemDistances[item.id]
                distance == null || distance <= searchRadiusKm
            }
            matchesCategory && matchesQuery && matchesRadius
        }

    val isEmptyResult: Boolean
        get() = !isLoading && error == null && visibleItems.isEmpty() && allItems.isNotEmpty()

    val isLocationEnabled: Boolean get() = userLocation != null

    companion object {
        const val DEFAULT_RADIUS_KM = 20f
        const val MIN_RADIUS_KM = 1f
        const val MAX_RADIUS_KM = 20f
    }
}
