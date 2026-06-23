package com.prezto.prezto.feature_explore.presentation.item_detail

import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.RentalQuote

data class ItemDetailState(
    val isLoading: Boolean = true,
    val item: Item? = null,
    /** Cantidad de días elegida (calculadora rápida en el detalle). */
    val selectedDays: Int = 1,
    /** Cotización dinámica recalculada según [selectedDays]. */
    val quote: RentalQuote? = null,
    val error: String? = null
)
