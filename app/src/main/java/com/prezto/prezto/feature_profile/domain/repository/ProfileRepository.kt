package com.prezto.prezto.feature_profile.domain.repository

import com.prezto.prezto.feature_profile.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

/**
 * Fuente única de verdad del perfil. Expone el perfil como [StateFlow] para que ProfileScreen
 * y EditProfileScreen permanezcan siempre sincronizados.
 */
interface ProfileRepository {

    val profile: StateFlow<UserProfile>

    suspend fun updateProfile(fullName: String, location: String, avatarUri: String?)

    /** Simula la validación KYC del DNI y marca la identidad como verificada. */
    suspend fun verifyIdentity()
}
