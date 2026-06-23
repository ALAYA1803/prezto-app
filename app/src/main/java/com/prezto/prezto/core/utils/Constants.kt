package com.prezto.prezto.core.utils

object Constants {

    /**
     * Tarifa de Protección Prezto: micro-seguro obligatorio por alquiler.
     * Alimenta un fondo interno para reparaciones/desgastes y protege al propietario.
     * Expresada como fracción del subtotal (8%).
     */
    const val PROTECTION_FEE_RATE = 0.08

    /**
     * Garantía base = tarifa diaria x este multiplicador. Se "congela" (pre-autoriza)
     * en la tarjeta, no se debita. La Garantía Dinámica luego la reduce según el TrustScore.
     */
    const val GUARANTEE_DAILY_MULTIPLIER = 4.0

    /**
     * Comisión de la plataforma Prezto sobre el alquiler. Se descuenta de lo que recibe
     * el propietario (modelo P2P + Escrow). Expresada como fracción (10%).
     */
    const val PREZTO_COMMISSION_RATE = 0.10

    /** Símbolo de moneda (Soles peruanos). */
    const val CURRENCY_SYMBOL = "S/"
}
