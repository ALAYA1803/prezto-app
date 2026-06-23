package com.prezto.prezto.feature_explore.data.repository

import com.prezto.prezto.feature_explore.domain.model.Category
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.ItemCondition
import com.prezto.prezto.core.domain.model.TrustScore
import com.prezto.prezto.feature_explore.domain.model.Owner
import com.prezto.prezto.feature_explore.domain.repository.ExploreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockExploreRepositoryImpl : ExploreRepository {

    private val mockCategories = listOf(
        Category(id = "c1", name = "Herramientas Eléctricas"),
        Category(id = "c2", name = "Fotografía y Video"),
        Category(id = "c3", name = "Jardinería"),
        Category(id = "c4", name = "Limpieza"),
        Category(id = "c5", name = "Construcción")
    )

    // Propietarios (Value Objects con su TrustScore para la gamificación de confianza)
    private val rodrigo = Owner("user_101", "Rodrigo A.", TrustScore(4.9), isVerified = true)
    private val carla = Owner("user_102", "Carla M.", TrustScore(4.7), isVerified = true)
    private val luis = Owner("user_103", "Luis P.", TrustScore(3.6), isVerified = false)
    private val sofia = Owner("user_104", "Sofía R.", TrustScore(5.0), isVerified = true)
    private val marco = Owner("user_105", "Marco T.", TrustScore(4.2), isVerified = true)

    private val mockItems = mutableListOf(
        Item(
            id = "item_001",
            categoryId = "c1",
            title = "Taladro Percutor Bosch 750W",
            description = "Taladro percutor en perfecto estado, ideal para perforar concreto y madera. Incluye maletín y set de brocas básicas.",
            dailyRate = 25.0,
            hourlyRate = 5.0,
            currentCondition = ItemCondition.GOOD,
            isAvailable = true,
            imageUrl = "https://images.unsplash.com/photo-1504148455328-c376907d081c?q=80&w=800&auto=format&fit=crop",
            owner = rodrigo,
            latitude = -12.0776,
            longitude = -77.0896
        ),
        Item(
            id = "item_002",
            categoryId = "c2",
            title = "Cámara Sony Alpha a7 III",
            description = "Cámara mirrorless full-frame. Solo cuerpo. Ideal para eventos o sesiones de fotos puntuales. Requiere cuidado extremo.",
            dailyRate = 120.0,
            hourlyRate = 25.0,
            currentCondition = ItemCondition.NEW,
            isAvailable = true,
            imageUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=800&auto=format&fit=crop",
            owner = carla,
            latitude = -12.1027,
            longitude = -77.0286
        ),
        Item(
            id = "item_003",
            categoryId = "c4",
            title = "Hidrolavadora Kärcher K5",
            description = "Hidrolavadora de alta presión, perfecta para patios, fachadas y autos. Manguera de 8m y boquillas incluidas.",
            dailyRate = 45.0,
            hourlyRate = 10.0,
            currentCondition = ItemCondition.GOOD,
            isAvailable = true,
            imageUrl = "https://images.unsplash.com/photo-1610433572201-110753c6cff9?q=80&w=800&auto=format&fit=crop",
            owner = sofia,
            latitude = -12.1464,
            longitude = -77.0220
        ),
        Item(
            id = "item_004",
            categoryId = "c5",
            title = "Escalera Telescópica de Aluminio 4m",
            description = "Escalera plegable extensible hasta 4 metros. Liviana y resistente, soporta hasta 150kg. Ideal para pintura y mantenimiento.",
            dailyRate = 18.0,
            hourlyRate = 4.0,
            currentCondition = ItemCondition.FAIR,
            isAvailable = true,
            imageUrl = "https://images.unsplash.com/photo-1620230874645-0d2b7c8f2f3a?q=80&w=800&auto=format&fit=crop",
            owner = luis,
            latitude = -12.0464,
            longitude = -77.0428
        ),
        Item(
            id = "item_005",
            categoryId = "c3",
            title = "Cortacésped a Gasolina Honda",
            description = "Cortacésped autopropulsado con motor Honda. Recién mantenido. Ideal para jardines medianos y grandes.",
            dailyRate = 60.0,
            hourlyRate = 15.0,
            currentCondition = ItemCondition.GOOD,
            isAvailable = true,
            imageUrl = "https://images.unsplash.com/photo-1599629954294-14df9ec8506b?q=80&w=800&auto=format&fit=crop",
            owner = marco,
            latitude = -11.9900,
            longitude = -77.0600
        )
    )

    override fun getCategories(): Flow<List<Category>> = flowOf(mockCategories)

    override fun getFeaturedItems(): Flow<List<Item>> =
        flowOf(mockItems.filter { it.isAvailable })

    override fun getItemsByCategory(categoryId: String): Flow<List<Item>> =
        flowOf(mockItems.filter { it.categoryId == categoryId })

    override suspend fun getItemById(itemId: String): Item? =
        mockItems.find { it.id == itemId }

    override suspend fun publishItem(item: Item) {
        // El ítem nuevo se agrega al inicio con sus coordenadas fijas.
        mockItems.add(0, item)
    }
}
