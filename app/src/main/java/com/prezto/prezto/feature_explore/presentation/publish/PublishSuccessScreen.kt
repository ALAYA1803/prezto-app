package com.prezto.prezto.feature_explore.presentation.publish

import androidx.compose.runtime.Composable
import com.prezto.prezto.core.designsystem.SuccessConfirmationScreen

/**
 * Happy Path tras publicar una herramienta. Stateless: solo recibe la navegación.
 */
@Composable
fun PublishSuccessScreen(
    onBackToHome: () -> Unit
) {
    SuccessConfirmationScreen(
        title = "¡Herramienta publicada con éxito!",
        message = "Ya está visible para los arrendatarios de tu zona. Te avisaremos cuando alguien quiera alquilarla.",
        primaryButtonText = "Volver al Inicio",
        onPrimaryClick = onBackToHome
    )
}
