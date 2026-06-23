package com.prezto.prezto.feature_profile.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    init {
        // Precarga los valores actuales desde la fuente única de verdad.
        val current = profileRepository.profile.value
        _state.update {
            it.copy(
                fullName = current.fullName,
                location = current.location,
                avatarUri = current.avatarUri
            )
        }
    }

    fun onFullNameChange(value: String) = _state.update { it.copy(fullName = value) }
    fun onLocationChange(value: String) = _state.update { it.copy(location = value) }
    fun onAvatarSelected(uri: String?) = _state.update { it.copy(avatarUri = uri ?: it.avatarUri) }

    fun save() {
        val s = _state.value
        if (!s.isValid || s.isSaving) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            profileRepository.updateProfile(s.fullName.trim(), s.location.trim(), s.avatarUri)
            _state.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
