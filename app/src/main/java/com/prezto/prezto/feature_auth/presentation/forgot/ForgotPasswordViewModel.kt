package com.prezto.prezto.feature_auth.presentation.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ForgotPasswordViewModel @Inject constructor(
    private val validateEmail: ValidateEmail
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email, emailError = null, error = null) }
    }

    fun sendResetLink() {
        val result = validateEmail(_state.value.email)
        if (!result.successful) {
            _state.update { it.copy(emailError = result.errorMessage) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // Simulación de envío del enlace (futuro: AuthRepository.sendPasswordReset()).
            delay(1500)
            _state.update { it.copy(isLoading = false, isEmailSent = true) }
        }
    }
}
