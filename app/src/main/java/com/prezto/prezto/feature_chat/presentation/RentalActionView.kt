package com.prezto.prezto.feature_chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prezto.prezto.feature_chat.domain.model.RentalStatus

/**
 * Tarjeta de acciones del ciclo de vida del alquiler (Check-in/Check-out fotográfico).
 * Reutilizable en el Chat o en un detalle de reserva. Stateless: el caller lanza el ImagePicker.
 */
@Composable
fun RentalActionView(
    status: RentalStatus,
    onConfirmDelivery: () -> Unit,
    onConfirmReturn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusBadge(status = status)

        Spacer(modifier = Modifier.width(12.dp))

        when (status) {
            RentalStatus.AWAITING_DELIVERY -> {
                Button(
                    onClick = onConfirmDelivery,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirmar Entrega", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
            }

            RentalStatus.IN_PROGRESS -> {
                OutlinedButton(
                    onClick = onConfirmReturn,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirmar Devolución", fontWeight = FontWeight.Bold)
                }
            }

            RentalStatus.FINISHED -> {
                Text(
                    text = "Alquiler finalizado con éxito.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: RentalStatus) {
    val color = when (status) {
        RentalStatus.AWAITING_DELIVERY -> MaterialTheme.colorScheme.secondary
        RentalStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        RentalStatus.FINISHED -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = status.label,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}
