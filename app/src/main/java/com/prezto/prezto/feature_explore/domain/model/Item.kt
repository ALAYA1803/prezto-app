package com.prezto.prezto.feature_explore.domain.model

enum class ItemCondition(val label: String) {
    NEW("Nuevo"),
    GOOD("Buen estado"),
    FAIR("Aceptable"),
    NEEDS_REPAIR("Requiere reparación")
}

/**
 * Entidad rica del dominio: una herramienta publicada en el marketplace P2P.
 * Embebe al [Owner] (con su TrustScore) porque la confianza es inseparable del ítem
 * en el flujo de descubrimiento.
 */
data class Item(
    val id: String,
    val categoryId: String,
    val title: String,
    val description: String,
    val dailyRate: Double,
    val hourlyRate: Double,
    val currentCondition: ItemCondition,
    val isAvailable: Boolean,
    val imageUrl: String,
    val owner: Owner
)
