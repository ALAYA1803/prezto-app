package com.prezto.prezto.feature_auth.presentation.verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.data.session.AuthTokens
import com.prezto.prezto.core.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        VerificationState(phone = savedStateHandle.get<String>("phone").orEmpty())
    )
    val state: StateFlow<VerificationState> = _state.asStateFlow()

    init {
        startResendCountdown()
    }

    fun onOtpChange(value: String) {
        if (value.all { it.isDigit() } && value.length <= VerificationState.OTP_LENGTH) {
            _state.update { it.copy(otp = value, otpError = null) }
        }
    }

    fun onResendCode() {
        if (!_state.value.canResend) return
        // TODO(P0): AuthRepository.resendOtp(phone). Por ahora solo reinicia el contador.
        startResendCountdown()
    }

    private fun startResendCountdown() {
        viewModelScope.launch {
            _state.update { it.copy(secondsRemaining = COUNTDOWN_SECONDS) }
            while (_state.value.secondsRemaining > 0) {
                delay(1000)
                _state.update { it.copy(secondsRemaining = it.secondsRemaining - 1) }
            }
        }
    }

    fun verify() {
        val current = _state.value
        if (!current.isOtpComplete) {
            _state.update { it.copy(otpError = "Ingresa los ${VerificationState.OTP_LENGTH} dígitos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isVerifying = true, otpError = null) }
            // TODO(P0): AuthRepository.verifyOtp(phone, otp) -> tokens reales.
            delay(1500)
            // Solo aquí emitimos la sesión (AUTHENTICATED) tras verificar el celular.
            sessionManager.saveTokens(
                AuthTokens(accessToken = "mock_access_token", refreshToken = "mock_refresh_token")
            )
            _state.update { it.copy(isVerifying = false, isVerified = true) }
        }
    }

    private companion object {
        const val COUNTDOWN_SECONDS = 60
    }
}
