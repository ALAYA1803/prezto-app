package com.prezto.prezto.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val PreztoDarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    background = CharcoalBlack,
    surface = DarkGraySurface,
    onPrimary = CharcoalBlack,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun PreztoTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PreztoDarkColorScheme,
        content = content
    )
}