package com.prezto.prezto.feature_explore.domain.model

import com.prezto.prezto.core.domain.model.TrustLevel
import com.prezto.prezto.core.domain.model.TrustScore
import com.prezto.prezto.core.utils.Constants

/**
 * Value Object de la "Garantía Dinámica" (regla de negocio crítica).
 * El monto se CONGELA (pre-autoriza) en la tarjeta, nunca se debita. La gamificación
 * reduce ese monto según el TrustScore del arrendatario: a mayor confianza, menor garantía.
 */
data class Guarantee(
    val baseAmount: Double,
    val finalAmount: Double,
    val discountRate: Double
) {
    val hasDiscount: Boolean get() = discountRate > 0.0
    val savedAmount: Double get() = baseAmount - finalAmount

    companion object {
        fun forItem(item: Item, renterTrust: TrustScore): Guarantee {
            val base = item.dailyRate * Constants.GUARANTEE_DAILY_MULTIPLIER
            val discount = when (renterTrust.level) {
                TrustLevel.ELITE -> 0.50
                TrustLevel.TRUSTED -> 0.30
                TrustLevel.STANDARD -> 0.10
                TrustLevel.NEW -> 0.0
            }
            return Guarantee(
                baseAmount = base,
                finalAmount = base * (1 - discount),
                discountRate = discount
            )
        }
    }
}
