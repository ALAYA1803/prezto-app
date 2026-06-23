package com.prezto.prezto.feature_profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.session.SessionManager
import com.prezto.prezto.feature_profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState(isLoading = true))
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            profileRepository.profile.collect { profile ->
                _state.update { it.copy(isLoading = false, profile = profile) }
            }
        }
    }

    fun verifyIdentity() {
        if (_state.value.isVerifyingIdentity) return
        viewModelScope.launch {
            _state.update { it.copy(isVerifyingIdentity = true) }
            profileRepository.verifyIdentity()
            _state.update { it.copy(isVerifyingIdentity = false) }
        }
    }

    /**
     * Limpia la sesión cifrada. La navegación al login la dispara el AuthCoordinator al
     * observar el cambio a Unauthenticated.
     */
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }
}
