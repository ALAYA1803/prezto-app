package com.prezto.prezto.feature_auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.session.AuthTokens
import com.prezto.prezto.core.data.session.SessionManager
import com.prezto.prezto.core.domain.validation.ValidateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email, emailError = null, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password, passwordError = null, error = null) }
    }

    fun login() {
        val s = _state.value
        var hasError = false

        val emailResult = validateEmail(s.email)
        if (!emailResult.successful) {
            _state.update { it.copy(emailError = emailResult.errorMessage) }
            hasError = true
        }
        // En login no exigimos fortaleza (eso es del registro); solo que no esté vacía.
        if (s.password.isBlank()) {
            _state.update { it.copy(passwordError = "Ingresa tu contraseña") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            delay(1500)
            if (s.password == "123456") {
                // TODO(P0): reemplazar por los tokens reales del AuthRepository.
                sessionManager.saveTokens(
                    AuthTokens(accessToken = "mock_access_token", refreshToken = "mock_refresh_token")
                )
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Credenciales incorrectas. Para probar usa la clave: 123456"
                    )
                }
            }
        }
    }
}
