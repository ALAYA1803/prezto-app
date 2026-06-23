package com.prezto.prezto.feature_explore.presentation.publish

import com.prezto.prezto.feature_explore.domain.model.Category
import com.prezto.prezto.feature_explore.domain.model.ItemCondition

data class PublishState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val dailyRate: String = "",
    val dailyRateError: String? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val condition: ItemCondition = ItemCondition.GOOD,
    /** URI de la imagen elegida en la galería (String para sobrevivir a recreaciones). */
    val selectedImageUri: String? = null,
    /** Coordenadas capturadas del dispositivo (ubicación del artículo). */
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
) {
    /** Precio parseado (null si aún no es válido). */
    val parsedRate: Double?
        get() = dailyRate.toDoubleOrNull()?.takeIf { it > 0.0 }

    /**
     * Validación reactiva para habilitar el botón: todos los obligatorios llenos
     * y precio > 0. No muta el estado (los errores se muestran al intentar publicar).
     */
    val isFormValid: Boolean
        get() = title.trim().length >= 5 &&
                description.trim().length >= 20 &&
                parsedRate != null &&
                selectedCategoryId != null &&
                latitude != null && longitude != null

    val hasLocation: Boolean get() = latitude != null && longitude != null
}
