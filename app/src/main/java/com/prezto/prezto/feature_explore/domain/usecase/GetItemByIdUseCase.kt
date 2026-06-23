package com.prezto.prezto.feature_explore.domain.usecase

import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.repository.ExploreRepository
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    suspend operator fun invoke(itemId: String): Item? {
        return repository.getItemById(itemId)
    }
}