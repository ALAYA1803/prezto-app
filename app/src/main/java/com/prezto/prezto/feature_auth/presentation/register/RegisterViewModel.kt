package com.prezto.prezto.feature_auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prezto.prezto.core.domain.validation.ValidateDni
import com.prezto.prezto.core.domain.validation.ValidateEmail
import com.prezto.prezto.core.domain.validation.ValidateName
import com.prezto.prezto.core.domain.validation.ValidatePassword
import com.prezto.prezto.core.domain.validation.ValidatePhone
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateName: ValidateName,
    private val validateDni: ValidateDni,
    private val validatePhone: ValidatePhone,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onFullNameChanged(name: String) = _state.update { it.copy(fullName = name, fullNameError = null) }

    fun onDniChanged(dni: String) {
        if (dni.all { it.isDigit() } && dni.length <= 8) {
            _state.update { it.copy(dni = dni, dniError = null) }
        }
    }

    fun onPhoneChanged(phone: String) {
        if (phone.all { it.isDigit() } && phone.length <= 9) {
            _state.update { it.copy(phone = phone, phoneError = null) }
        }
    }

    fun onEmailChanged(email: String) = _state.update { it.copy(email = email, emailError = null) }
    fun onPasswordChanged(password: String) = _state.update { it.copy(password = password, passwordError = null) }

    /** Valida el Paso 1 (datos personales) y avanza al Paso 2 si todo es correcto. */
    fun onContinueToSecurity() {
        val s = _state.value
        var hasError = false

        validateName(s.fullName).let { if (!it.successful) { _state.update { st -> st.copy(fullNameError = it.errorMessage) }; hasError = true } }
        validateDni(s.dni).let { if (!it.successful) { _state.update { st -> st.copy(dniError = it.errorMessage) }; hasError = true } }
        validatePhone(s.phone).let { if (!it.successful) { _state.update { st -> st.copy(phoneError = it.errorMessage) }; hasError = true } }
        validateEmail(s.email).let { if (!it.successful) { _state.update { st -> st.copy(emailError = it.errorMessage) }; hasError = true } }

        if (!hasError) _state.update { it.copy(currentStep = 2) }
    }

    fun onBackToPersonalData() = _state.update { it.copy(currentStep = 1) }

    /** Valida el Paso 2 (seguridad) y finaliza el registro. */
    fun register() {
        val s = _state.value
        val passwordResult = validatePassword(s.password)
        if (!passwordResult.successful) {
            _state.update { it.copy(passwordError = passwordResult.errorMessage) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // Crea la cuenta pero NO autentica: la sesión se emite tras verificar el OTP.
            delay(2000)
            _state.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }
}
