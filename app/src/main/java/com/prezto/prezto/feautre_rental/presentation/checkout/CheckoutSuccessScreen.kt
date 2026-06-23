package com.prezto.prezto.feautre_rental.presentation.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prezto.prezto.core.designsystem.SuccessConfirmationScreen

/**
 * Happy Path tras el Checkout (Escrow). Stateless: solo recibe la navegación.
 * El botón a "Chat Seguro" se habilitará cuando exista el módulo de chat; por ahora
 * el flujo se cierra de forma segura hacia el inicio.
 */
@Composable
fun CheckoutSuccessScreen(
    onBackToHome: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    SuccessConfirmationScreen(
        title = "¡Reserva confirmada!",
        message = "Los fondos están asegurados en la bóveda Prezto. Coordina la entrega con el propietario.",
        primaryButtonText = "Ir al Chat Seguro",
        onPrimaryClick = onNavigateToChat,
        secondaryButtonText = "Volver al Inicio",
        onSecondaryClick = onBackToHome,
        details = {
            NextStepRow(
                icon = Icons.Default.Shield,
                text = "El propietario solo recibe el pago cuando devuelvas la herramienta."
            )
            Spacer(modifier = Modifier.size(12.dp))
            NextStepRow(
                icon = Icons.AutoMirrored.Filled.Chat,
                text = "Contacta al propietario por el Chat Seguro para coordinar el punto de encuentro."
            )
        }
    )
}

@Composable
private fun NextStepRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
