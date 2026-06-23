package com.prezto.prezto.feature_chat.presentation

import com.prezto.prezto.feature_chat.domain.model.ChatMessage
import com.prezto.prezto.feature_chat.domain.model.EscrowStatus
import com.prezto.prezto.feature_chat.domain.model.RentalStatus

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val currentUserId: String = "",
    val inputText: String = "",
    val escrowStatus: EscrowStatus = EscrowStatus.PAID,
    val rentalStatus: RentalStatus = RentalStatus.AWAITING_DELIVERY,
    /** Aviso del Escudo Anti-Fuga al detectar datos de contacto en el mensaje. */
    val securityWarning: String? = null
) {
    /** Mientras el pago esté PENDING, el chat está bloqueado. */
    val isChatLocked: Boolean get() = escrowStatus == EscrowStatus.PENDING

    /** Tras la devolución (FINISHED), el chat queda en solo lectura. */
    val isReadOnly: Boolean get() = rentalStatus == RentalStatus.FINISHED
}
