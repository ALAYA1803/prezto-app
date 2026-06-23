package com.prezto.prezto.feature_profile.presentation.profile

import com.prezto.prezto.feature_profile.domain.model.UserProfile

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val isVerifyingIdentity: Boolean = false,
    val error: String? = null
)