package com.prezto.prezto.feautre_rental.presentation.checkout

import com.prezto.prezto.feature_explore.domain.model.Guarantee
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.RentalQuote

data class CheckoutState(
    val isLoading: Boolean = true,
    val item: Item? = null,
    val quote: RentalQuote? = null,
    val guarantee: Guarantee? = null,
    /** Nivel del arrendatario, para mostrar la recompensa de la Garantía Dinámica. */
    val renterTrustLabel: String = "",
    val days: Int = 1,
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    /** Índices de día (epoch/86.4M) ocupados que el calendario debe bloquear. */
    val blockedDayIndices: Set<Long> = emptySet(),
    val isProcessing: Boolean = false,
    val isPaid: Boolean = false,
    val error: String? = null
)
