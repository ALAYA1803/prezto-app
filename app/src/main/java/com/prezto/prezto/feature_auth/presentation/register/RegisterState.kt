package com.prezto.prezto.feature_auth.presentation.register

data class RegisterState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val dni: String = "",
    val dniError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    /** Paso del wizard: 1 = datos personales, 2 = seguridad. */
    val currentStep: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
