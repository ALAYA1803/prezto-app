package com.prezto.prezto.feature_profile.domain.model

import com.prezto.prezto.core.domain.model.TrustScore

/**
 * Entidad del perfil: el "currículum de confianza" del usuario.
 * Usa el Value Object [TrustScore] del shared kernel para la gamificación.
 */
data class UserProfile(
    val id: String,
    val fullName: String,
    val location: String,
    val trustScore: TrustScore,
    val isIdentityVerified: Boolean,
    val isEmailVerified: Boolean,
    val isPhoneVerified: Boolean,
    val successfulRentals: Int,
    val toolsPublished: Int,
    val avatarUri: String? = null
) {
    val initial: String
        get() = fullName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
}
