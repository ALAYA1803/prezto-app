package com.prezto.prezto.feature_auth.presentation.forgot

data class ForgotPasswordState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val error: String? = null
)
