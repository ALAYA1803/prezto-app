package com.prezto.prezto.feature_chat.domain.model

data class ChatMessage(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val content: String,
    val timestamp: Long,
    val isSystemMessage: Boolean = false
)

/**
 * Estado del Escrow para una conversación. El "Escudo Anti-Fuga" del chat depende de esto:
 * mientras esté PENDING, el chat permanece bloqueado.
 */
enum class EscrowStatus { PENDING, PAID }
