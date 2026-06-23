package com.prezto.prezto.feature_explore.presentation.item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.network.ErrorMapper
import com.prezto.prezto.core.util.logging.PreztoLogger
import com.prezto.prezto.feature_explore.domain.model.RentalQuote
import com.prezto.prezto.feature_explore.domain.usecase.GetItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val errorMapper: ErrorMapper,
    private val logger: PreztoLogger,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ItemDetailState())
    val state: StateFlow<ItemDetailState> = _state.asStateFlow()

    private val itemId: String? = savedStateHandle.get<String>("itemId")

    init {
        loadItem()
    }

    /** Reintento expuesto al ErrorView. */
    fun onRetry() = loadItem()

    fun onDaysChanged(delta: Int) {
        val item = _state.value.item ?: return
        val newDays = (_state.value.selectedDays + delta).coerceIn(1, MAX_DAYS)
        _state.update {
            it.copy(selectedDays = newDays, quote = RentalQuote.forDays(item, newDays))
        }
    }

    private fun loadItem() {
        val id = itemId
        if (id == null) {
            _state.update { it.copy(isLoading = false, error = "ID de herramienta no encontrado") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val result = getItemByIdUseCase(id)
                if (result != null) {
                    val days = _state.value.selectedDays
                    val quote = RentalQuote.forDays(result, days)
                    _state.update { it.copy(isLoading = false, item = result, quote = quote) }
                } else {
                    _state.update { it.copy(isLoading = false, error = "La herramienta ya no está disponible") }
                }
            } catch (e: Exception) {
                logger.error(TAG, "Error cargando el detalle del ítem $id", e)
                _state.update { it.copy(isLoading = false, error = errorMapper.map(e)) }
            }
        }
    }

    private companion object {
        const val TAG = "ItemDetailViewModel"
        const val MAX_DAYS = 30
    }
}
