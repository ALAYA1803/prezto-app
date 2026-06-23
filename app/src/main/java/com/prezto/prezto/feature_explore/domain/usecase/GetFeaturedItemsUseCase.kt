package com.prezto.prezto.feature_explore.domain.usecase

import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.repository.ExploreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeaturedItemsUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    operator fun invoke(): Flow<List<Item>> {
        return repository.getFeaturedItems()
    }
}