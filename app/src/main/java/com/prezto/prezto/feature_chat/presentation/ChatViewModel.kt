package com.prezto.prezto.feature_chat.presentation

import androidx.lifecycle.ViewModel
import com.prezto.prezto.feature_chat.domain.model.ChatMessage
import com.prezto.prezto.feature_chat.domain.model.EscrowStatus
import com.prezto.prezto.feature_chat.domain.model.RentalStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val currentUserId = "me"
    private val ownerId = "owner_101"

    private val _state = MutableStateFlow(
        ChatState(
            currentUserId = currentUserId,
            // Escrow PAID: el chat llega desbloqueado desde el Checkout exitoso.
            escrowStatus = EscrowStatus.PAID,
            messages = listOf(
                ChatMessage(
                    id = "sys_1",
                    senderId = "system",
                    receiverId = currentUserId,
                    content = "Pago confirmado. El chat seguro está activo. No compartas datos fuera de Prezto.",
                    timestamp = System.currentTimeMillis(),
                    isSystemMessage = true
                ),
                ChatMessage(
                    id = "m_1",
                    senderId = ownerId,
                    receiverId = currentUserId,
                    content = "¡Hola! Gracias por reservar. ¿A qué hora coordinamos la entrega?",
                    timestamp = System.currentTimeMillis()
                )
            )
        )
    )
    val state: StateFlow<ChatState> = _state.asStateFlow()

    // Evento de una sola vez para Snackbars (ej. funciones en desarrollo).
    private val _snackbar = Channel<String>()
    val snackbar = _snackbar.receiveAsFlow()

    fun onInputChange(text: String) {
        _state.update { it.copy(inputText = text, securityWarning = null) }
    }

    fun onSend() {
        val current = _state.value
        if (current.isChatLocked || current.isReadOnly) return
        val text = current.inputText.trim()
        if (text.isEmpty()) return

        // Escudo Anti-Fuga: bloquea el envío de números de teléfono o enlaces.
        if (containsContactInfo(text)) {
            _state.update {
                it.copy(securityWarning = "Por tu seguridad, el chat bloquea números de teléfono y enlaces.")
            }
            return
        }

        val message = ChatMessage(
            id = "m_${System.currentTimeMillis()}",
            senderId = currentUserId,
            receiverId = ownerId,
            content = text,
            timestamp = System.currentTimeMillis()
        )
        _state.update { it.copy(messages = it.messages + message, inputText = "", securityWarning = null) }
    }

    fun onSendLocation() {
        _snackbar.trySend("Función de ubicación en desarrollo")
    }

    /** Check-in fotográfico de entrega: pasa el alquiler a "En curso". */
    fun onConfirmDelivery(photoUri: String?) {
        if (_state.value.rentalStatus != RentalStatus.AWAITING_DELIVERY) return
        addSystemMessage("Entrega confirmada con foto de evidencia. El alquiler está en curso.")
        _state.update { it.copy(rentalStatus = RentalStatus.IN_PROGRESS) }
        _snackbar.trySend("Entrega confirmada")
    }

    /** Check-out fotográfico de devolución: finaliza el alquiler y cierra el chat. */
    fun onConfirmReturn(photoUri: String?) {
        if (_state.value.rentalStatus != RentalStatus.IN_PROGRESS) return
        addSystemMessage("Devolución confirmada. La conversación quedó finalizada.")
        _state.update { it.copy(rentalStatus = RentalStatus.FINISHED) }
        _snackbar.trySend("Devolución confirmada")
    }

    private fun addSystemMessage(content: String) {
        val msg = ChatMessage(
            id = "sys_${System.currentTimeMillis()}",
            senderId = "system",
            receiverId = currentUserId,
            content = content,
            timestamp = System.currentTimeMillis(),
            isSystemMessage = true
        )
        _state.update { it.copy(messages = it.messages + msg) }
    }

    private fun containsContactInfo(text: String): Boolean {
        val hasUrl = Regex("(https?://|www\\.)", RegexOption.IGNORE_CASE).containsMatchIn(text)
        // 6+ dígitos seguidos (con espacios/guiones opcionales) ~ número de teléfono.
        val digitCount = text.count { it.isDigit() }
        val hasPhone = digitCount >= 6
        return hasUrl || hasPhone
    }
}
