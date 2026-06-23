package com.prezto.prezto.feature_auth.presentation.splash

/** Destino al que el Splash debe enviar al usuario una vez resuelta la sesión. */
enum class SplashDestination { HOME, LOGIN }

data class SplashUiState(
    val isResolving: Boolean = true,
    val destination: SplashDestination? = null
)
