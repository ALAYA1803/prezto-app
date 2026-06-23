package com.prezto.prezto.feature_profile.data.repository

import com.prezto.prezto.core.domain.model.TrustScore
import com.prezto.prezto.feature_profile.domain.model.UserProfile
import com.prezto.prezto.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockProfileRepositoryImpl @Inject constructor() : ProfileRepository {

    private val _profile = MutableStateFlow(
        UserProfile(
            id = "usr_99",
            fullName = "Rodrigo A.",
            location = "San Miguel, Lima",
            trustScore = TrustScore(4.9),
            isIdentityVerified = false,
            isEmailVerified = true,
            isPhoneVerified = true,
            successfulRentals = 12,
            toolsPublished = 5,
            avatarUri = null
        )
    )
    override val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    override suspend fun updateProfile(fullName: String, location: String, avatarUri: String?) {
        _profile.update {
            it.copy(fullName = fullName, location = location, avatarUri = avatarUri)
        }
    }

    override suspend fun verifyIdentity() {
        // Simula el tiempo de validación del proveedor KYC.
        delay(2000)
        _profile.update { it.copy(isIdentityVerified = true) }
    }
}
