package com.prezto.prezto.feature_chat.domain.model

/**
 * Ciclo de vida operativo de un alquiler, reflejado en el chat y el inbox.
 * El chat queda en solo lectura cuando llega a [FINISHED].
 */
enum class RentalStatus(val label: String) {
    AWAITING_DELIVERY("Esperando entrega"),
    IN_PROGRESS("En curso"),
    FINISHED("Finalizado")
}
