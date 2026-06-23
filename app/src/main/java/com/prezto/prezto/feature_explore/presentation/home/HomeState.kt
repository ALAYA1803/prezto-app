package com.prezto.prezto.feature_explore.presentation.home

import com.prezto.prezto.feature_explore.domain.model.Category
import com.prezto.prezto.feature_explore.domain.model.Item

data class HomeState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val allItems: List<Item> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val error: String? = null
) {
    /**
     * Lista visible derivada en tiempo real del query + la categoría seleccionada.
     * Mantener el filtro como estado derivado evita desincronización con la fuente.
     */
    val visibleItems: List<Item>
        get() = allItems.filter { item ->
            val matchesCategory = selectedCategoryId == null || item.categoryId == selectedCategoryId
            val matchesQuery = searchQuery.isBlank() ||
                    item.title.contains(searchQuery.trim(), ignoreCase = true)
            matchesCategory && matchesQuery
        }

    val isEmptyResult: Boolean
        get() = !isLoading && error == null && visibleItems.isEmpty() && allItems.isNotEmpty()
}
