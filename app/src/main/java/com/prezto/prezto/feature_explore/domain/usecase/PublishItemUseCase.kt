package com.prezto.prezto.feature_explore.domain.usecase

import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.repository.ExploreRepository
import javax.inject.Inject

class PublishItemUseCase @Inject constructor(
    private val repository: ExploreRepository
) {
    suspend operator fun invoke(item: Item) = repository.publishItem(item)
}
