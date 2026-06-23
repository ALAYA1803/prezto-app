package com.prezto.prezto.feature_chat.domain.model

data class Conversation(
    val id: String,
    val otherUserName: String,
    val otherUserAvatarUrl: String?,
    val toolTitle: String,
    val lastMessage: String,
    val rentalStatus: RentalStatus,
    val hasUnread: Boolean = false
)
