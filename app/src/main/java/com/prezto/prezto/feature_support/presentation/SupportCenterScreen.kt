package com.prezto.prezto.feature_support.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Faq(val question: String, val answer: String)

private val faqs = listOf(
    Faq(
        "¿Cómo reportar una falla?",
        "Tienes hasta 2 horas tras el Check-in para reportar fallas ocultas desde el Centro de Ayuda > Reportar incidencia, adjuntando una foto."
    ),
    Faq(
        "¿Qué cubre la Tarifa de Protección Prezto?",
        "Es un micro-seguro obligatorio que cubre desgastes y reparaciones menores del artículo, protegiendo al propietario durante el alquiler."
    ),
    Faq(
        "¿Cómo funciona la bóveda (Escrow)?",
        "Tu pago queda retenido de forma segura. El propietario solo lo recibe cuando confirmas la devolución de la herramienta."
    ),
    Faq(
        "¿Por qué se congela una garantía?",
        "La garantía se pre-autoriza (no se debita) en tu tarjeta. Su monto baja a mayor Trust Score."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportCenterScreen(
    onNavigateBack: () -> Unit,
    onContactAgent: () -> Unit,
    onReportIncident: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Ayuda", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Preguntas frecuentes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            faqs.forEach { faq ->
                FaqCard(faq = faq)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            SupportAction(
                icon = Icons.Default.SupportAgent,
                title = "Contactar a un asesor",
                subtitle = "Chatea con el equipo de Soporte Prezto",
                onClick = onContactAgent
            )
            Spacer(modifier = Modifier.height(12.dp))
            SupportAction(
                icon = Icons.Default.ReportProblem,
                title = "Reportar incidencia",
                subtitle = "¿Un problema con un alquiler activo?",
                onClick = onReportIncident
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FaqCard(faq: Faq) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = tween(250))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = faq.question,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = faq.answer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SupportAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
