package com.prezto.prezto.feature_profile.presentation.edit

data class EditProfileState(
    val fullName: String = "",
    val location: String = "",
    val avatarUri: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
) {
    val isValid: Boolean
        get() = fullName.trim().length >= 3 && location.isNotBlank()
}
