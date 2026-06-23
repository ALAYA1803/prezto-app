package com.prezto.prezto.feautre_rental.presentation.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prezto.prezto.core.utils.toSoles
import com.prezto.prezto.feature_explore.domain.model.Guarantee
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.RentalQuote
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DAY_MILLIS = 86_400_000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onNavigateBack: () -> Unit,
    onPaymentSuccess: () -> Unit = {},
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.isPaid) {
        if (state.isPaid) onPaymentSuccess()
    }

    if (showDatePicker) {
        DateRangePickerDialog(
            initialStart = state.startDateMillis,
            initialEnd = state.endDateMillis,
            blockedDayIndices = state.blockedDayIndices,
            onConfirm = { start, end ->
                viewModel.onDatesSelected(start, end)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Confirmar Alquiler",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            val quote = state.quote
            if (quote != null && !state.isLoading) {
                PayBottomBar(
                    total = quote.total,
                    isProcessing = state.isProcessing,
                    onConfirm = viewModel::onConfirmPayment
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )

                state.error != null -> Text(
                    text = state.error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )

                state.item != null && state.quote != null && state.guarantee != null -> {
                    AnimatedVisibility(visible = true, enter = fadeIn()) {
                        CheckoutContent(
                            item = state.item!!,
                            quote = state.quote!!,
                            guarantee = state.guarantee!!,
                            renterTrustLabel = state.renterTrustLabel,
                            startDateMillis = state.startDateMillis,
                            endDateMillis = state.endDateMillis,
                            onPickDates = { showDatePicker = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckoutContent(
    item: Item,
    quote: RentalQuote,
    guarantee: Guarantee,
    renterTrustLabel: String,
    startDateMillis: Long?,
    endDateMillis: Long?,
    onPickDates: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ItemSummaryHeader(item = item, days = quote.days)

        Spacer(modifier = Modifier.height(20.dp))
        DateRangeCard(
            startDateMillis = startDateMillis,
            endDateMillis = endDateMillis,
            days = quote.days,
            onPickDates = onPickDates
        )

        Spacer(modifier = Modifier.height(20.dp))
        EscrowCard()

        Spacer(modifier = Modifier.height(20.dp))
        FinancialBreakdownCard(quote = quote)

        Spacer(modifier = Modifier.height(20.dp))
        DynamicGuaranteeCard(guarantee = guarantee, renterTrustLabel = renterTrustLabel)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ItemSummaryHeader(item: Item, days: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$days día${if (days > 1) "s" else ""} · ${item.dailyRate.toSoles()}/día",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DateRangeCard(
    startDateMillis: Long?,
    endDateMillis: Long?,
    days: Int,
    onPickDates: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd MMM", Locale("es", "PE")) }
    val rangeText = if (startDateMillis != null && endDateMillis != null) {
        "${formatter.format(Date(startDateMillis))} - ${formatter.format(Date(endDateMillis))}"
    } else {
        "Selecciona las fechas"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onPickDates() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Fechas del alquiler",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$rangeText · $days día${if (days > 1) "s" else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "Cambiar",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateRangePickerDialog(
    initialStart: Long?,
    initialEnd: Long?,
    blockedDayIndices: Set<Long>,
    onConfirm: (Long, Long) -> Unit,
    onDismiss: () -> Unit
) {
    val selectableDates = remember(blockedDayIndices) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Bloquea los días ocupados (mock) para que no puedan elegirse.
                return (utcTimeMillis / DAY_MILLIS) !in blockedDayIndices
            }
        }
    }
    val rangeState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStart,
        initialSelectedEndDateMillis = initialEnd,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val start = rangeState.selectedStartDateMillis
                    val end = rangeState.selectedEndDateMillis
                    if (start != null && end != null) onConfirm(start, end)
                },
                enabled = rangeState.selectedStartDateMillis != null &&
                        rangeState.selectedEndDateMillis != null
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DateRangePicker(
            state = rangeState,
            modifier = Modifier.weight(1f),
            title = {
                Text(
                    text = "Selecciona el rango de alquiler",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
    }
}

@Composable
private fun EscrowCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = "Pago protegido en bóveda",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Tu dinero está seguro. El propietario solo lo recibirá cuando devuelvas la herramienta.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FinancialBreakdownCard(quote: RentalQuote) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "Resumen de pago",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))

        AmountRow("Subtotal alquiler", quote.subtotal.toSoles())
        Spacer(modifier = Modifier.height(10.dp))
        AmountRow("Tarifa de Protección Prezto", quote.protectionFee.toSoles())

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total a pagar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = quote.total.toSoles(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun DynamicGuaranteeCard(guarantee: Guarantee, renterTrustLabel: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Garantía",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = guarantee.finalAmount.toSoles(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Monto congelado en tu tarjeta (No se debitará).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Recompensa de la Garantía Dinámica: aparece animada si el TrustScore la reduce.
        AnimatedVisibility(
            visible = guarantee.hasDiscount,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Garantía reducida ${(guarantee.discountRate * 100).toInt()}%",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Recompensa por tu nivel $renterTrustLabel · ahorras ${guarantee.savedAmount.toSoles()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AmountRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PayBottomBar(
    total: Double,
    isProcessing: Boolean,
    onConfirm: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        shadowElevation = 24.dp
    ) {
        Button(
            onClick = onConfirm,
            enabled = !isProcessing,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isProcessing) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pagar y Retener Fondos · ${total.toSoles()}",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
