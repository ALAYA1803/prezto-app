package com.prezto.prezto.feature_auth.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.session.AuthState
import com.prezto.prezto.core.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        resolveSession()
    }

    private fun resolveSession() {
        viewModelScope.launch {
            // Tiempo mínimo de branding para que la animación del logo se aprecie.
            delay(SPLASH_MIN_DURATION_MS)

            val state = sessionManager.authState.first()
            val destination = when (state) {
                AuthState.Authenticated -> SplashDestination.HOME
                else -> SplashDestination.LOGIN
            }
            _uiState.update { it.copy(isResolving = false, destination = destination) }
        }
    }

    private companion object {
        const val SPLASH_MIN_DURATION_MS = 1500L
    }
}
