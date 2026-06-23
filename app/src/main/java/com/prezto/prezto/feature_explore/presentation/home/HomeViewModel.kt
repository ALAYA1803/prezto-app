package com.prezto.prezto.feature_explore.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.network.ErrorMapper
import com.prezto.prezto.core.util.logging.PreztoLogger
import com.prezto.prezto.feature_explore.domain.usecase.GetCategoriesUseCase
import com.prezto.prezto.feature_explore.domain.usecase.GetFeaturedItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getFeaturedItemsUseCase: GetFeaturedItemsUseCase,
    private val errorMapper: ErrorMapper,
    private val logger: PreztoLogger
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadHomeData()
    }

    /** Reintento expuesto al ErrorView. */
    fun onRetry() = loadHomeData()

    fun onSearchQueryChange(query: String) =
        _state.update { it.copy(searchQuery = query) }

    /** Selecciona o deselecciona (toggle) una categoría para filtrar. */
    fun onCategorySelected(categoryId: String) = _state.update {
        val newSelection = if (it.selectedCategoryId == categoryId) null else categoryId
        it.copy(selectedCategoryId = newSelection)
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .onStart { _state.update { it.copy(isLoading = true, error = null) } }
                .catch { exception -> handleError(exception) }
                .collect { categoriesList ->
                    _state.update { it.copy(categories = categoriesList) }
                }
            getFeaturedItemsUseCase()
                .catch { exception -> handleError(exception) }
                .collect { itemsList ->
                    _state.update { it.copy(allItems = itemsList, isLoading = false) }
                }
        }
    }

    private fun handleError(exception: Throwable) {
        logger.error(TAG, "Error cargando el Home", exception)
        _state.update { it.copy(isLoading = false, error = errorMapper.map(exception)) }
    }

    private companion object {
        const val TAG = "HomeViewModel"
    }
}
