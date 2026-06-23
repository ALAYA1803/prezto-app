package com.prezto.prezto.feature_chat.presentation.inbox

import androidx.lifecycle.ViewModel
import com.prezto.prezto.feature_chat.domain.model.Conversation
import com.prezto.prezto.feature_chat.domain.model.RentalStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor() : ViewModel() {

    private val _conversations = MutableStateFlow(mockConversations())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    private fun mockConversations(): List<Conversation> = listOf(
        Conversation(
            id = "c1",
            otherUserName = "Sofía R.",
            otherUserAvatarUrl = null,
            toolTitle = "Hidrolavadora Kärcher K5",
            lastMessage = "Perfecto, te espero a las 3pm.",
            rentalStatus = RentalStatus.IN_PROGRESS,
            hasUnread = true
        ),
        Conversation(
            id = "c2",
            otherUserName = "Carla M.",
            otherUserAvatarUrl = null,
            toolTitle = "Cámara Sony Alpha a7 III",
            lastMessage = "Pago confirmado. Coordinemos la entrega.",
            rentalStatus = RentalStatus.AWAITING_DELIVERY,
            hasUnread = false
        ),
        Conversation(
            id = "c3",
            otherUserName = "Marco T.",
            otherUserAvatarUrl = null,
            toolTitle = "Cortacésped a Gasolina Honda",
            lastMessage = "Gracias por el alquiler.",
            rentalStatus = RentalStatus.FINISHED,
            hasUnread = false
        )
    )
}
