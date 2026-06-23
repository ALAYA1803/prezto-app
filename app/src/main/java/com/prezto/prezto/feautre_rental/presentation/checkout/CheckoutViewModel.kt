package com.prezto.prezto.feautre_rental.presentation.checkout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.domain.model.TrustScore
import com.prezto.prezto.feature_explore.domain.model.Guarantee
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.RentalQuote
import com.prezto.prezto.feature_explore.domain.usecase.GetItemByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state.asStateFlow()

    // MOCK: el TrustScore del arrendatario actual vendrá de un UserRepository real.
    private val currentRenterTrust = TrustScore(4.9)

    private val initialDays: Int = savedStateHandle.get<String>("days")?.toIntOrNull()?.coerceAtLeast(1) ?: 1

    init {
        savedStateHandle.get<String>("itemId")?.let { loadCheckout(it) }
            ?: _state.update { it.copy(isLoading = false, error = "Herramienta no encontrada") }
    }

    private fun loadCheckout(itemId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val item = getItemByIdUseCase(itemId)
                if (item == null) {
                    _state.update { it.copy(isLoading = false, error = "La herramienta ya no está disponible") }
                    return@launch
                }
                val todayIndex = System.currentTimeMillis() / DAY_MILLIS
                val start = todayIndex * DAY_MILLIS
                val end = (todayIndex + initialDays - 1) * DAY_MILLIS
                _state.update {
                    it.copy(
                        isLoading = false,
                        item = item,
                        days = initialDays,
                        startDateMillis = start,
                        endDateMillis = end,
                        blockedDayIndices = mockBlockedIndices(todayIndex),
                        renterTrustLabel = currentRenterTrust.level.label,
                        quote = RentalQuote.forDays(item, initialDays),
                        guarantee = Guarantee.forItem(item, currentRenterTrust)
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = "Error de conexión: ${e.message}") }
            }
        }
    }

    /** Recalcula la cotización al elegir un rango de fechas en el calendario. */
    fun onDatesSelected(startMillis: Long, endMillis: Long) {
        val item = _state.value.item ?: return
        val days = (((endMillis - startMillis) / DAY_MILLIS) + 1).toInt().coerceAtLeast(1)
        _state.update {
            it.copy(
                startDateMillis = startMillis,
                endDateMillis = endMillis,
                days = days,
                quote = RentalQuote.forDays(item, days)
            )
        }
    }

    fun onConfirmPayment() {
        if (_state.value.isProcessing) return
        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true, error = null) }
            // Simulación de tokenización/retención en bóveda (Escrow).
            delay(1500)
            _state.update { it.copy(isProcessing = false, isPaid = true) }
        }
    }

    // MOCK: días ocupados de la herramienta (hoy+3 y hoy+4) que el calendario bloqueará.
    private fun mockBlockedIndices(todayIndex: Long): Set<Long> =
        setOf(todayIndex + 3, todayIndex + 4)

    private companion object {
        const val DAY_MILLIS = 86_400_000L
    }
}
