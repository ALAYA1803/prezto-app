package com.prezto.prezto.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prezto.prezto.core.domain.model.TrustScore

private val GoldStar = Color(0xFFFFC107)

/**
 * Píldora reutilizable que muestra el Trust Score con una estrella.
 * Componente del design system: se usa en ItemCard, ItemDetail y Perfil.
 */
@Composable
fun TrustBadge(
    trustScore: TrustScore,
    modifier: Modifier = Modifier,
    showLevel: Boolean = false,
    onDark: Boolean = false
) {
    val containerColor = if (onDark) Color.Black.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surface
    val contentColor = if (onDark) Color.White else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .background(containerColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = GoldStar,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = if (showLevel) "${trustScore.formatted} · ${trustScore.level.label}" else trustScore.formatted,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}
