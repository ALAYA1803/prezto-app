package com.prezto.prezto.feature_explore.domain.model

import com.prezto.prezto.core.utils.Constants

/**
 * Value Object que desglosa de forma transparente el costo de un alquiler.
 * Encapsula la regla de negocio de la "Tarifa de Protección Prezto" (micro-seguro
 * obligatorio). Es backend-ready: el día de mañana el cálculo puede moverse al servidor
 * y la UI seguirá consumiendo este mismo contrato.
 */
data class RentalQuote(
    val days: Int,
    val subtotal: Double,
    val protectionFee: Double,
    val total: Double
) {
    companion object {
        /** Cotiza [days] días (mínimo 1) para [item], aplicando la Tarifa de Protección. */
        fun forDays(item: Item, days: Int = 1): RentalQuote {
            val safeDays = days.coerceAtLeast(1)
            val subtotal = item.dailyRate * safeDays
            val protectionFee = subtotal * Constants.PROTECTION_FEE_RATE
            return RentalQuote(
                days = safeDays,
                subtotal = subtotal,
                protectionFee = protectionFee,
                total = subtotal + protectionFee
            )
        }
    }
}
