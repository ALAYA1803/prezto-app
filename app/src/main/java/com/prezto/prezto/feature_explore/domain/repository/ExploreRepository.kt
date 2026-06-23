package com.prezto.prezto.feature_explore.domain.repository

import com.prezto.prezto.feature_explore.domain.model.Category
import com.prezto.prezto.feature_explore.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {
    fun getCategories(): Flow<List<Category>>
    fun getFeaturedItems(): Flow<List<Item>>
    fun getItemsByCategory(categoryId: String): Flow<List<Item>>
    suspend fun getItemById(itemId: String): Item?
}