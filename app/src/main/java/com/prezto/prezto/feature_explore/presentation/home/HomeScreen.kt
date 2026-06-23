package com.prezto.prezto.feature_explore.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prezto.prezto.core.designsystem.EmptyView
import com.prezto.prezto.core.designsystem.ErrorView
import com.prezto.prezto.feature_explore.domain.model.Category
import com.prezto.prezto.feature_explore.presentation.home.components.ItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToPublish: () -> Unit,
    onNavigateToInbox: () -> Unit = {},
    hasNotifications: Boolean = false
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Bienvenido de nuevo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "¿Qué alquilamos hoy?",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = onNavigateToInbox) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        if (hasNotifications) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .size(8.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            )
                        }
                    }
                    IconButton(
                        onClick = onNavigateToProfile,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Mi Perfil",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToPublish,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Publicar Herramienta")
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
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                state.error != null -> {
                    ErrorView(
                        message = state.error ?: "",
                        onRetry = viewModel::onRetry
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        item {
                            SearchField(
                                query = state.searchQuery,
                                onQueryChange = viewModel::onSearchQueryChange
                            )
                        }
                        item {
                            Text(
                                text = "Categorías",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            CategoriesRow(
                                categories = state.categories,
                                selectedCategoryId = state.selectedCategoryId,
                                onCategoryClick = viewModel::onCategorySelected
                            )
                        }
                        item {
                            Text(
                                text = if (state.selectedCategoryId == null && state.searchQuery.isBlank())
                                    "Herramientas Destacadas" else "Resultados",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        if (state.isEmptyResult) {
                            item {
                                EmptyView(
                                    title = "No se encontraron herramientas",
                                    description = "Prueba con otra búsqueda o cambia de categoría.",
                                    modifier = Modifier.padding(top = 32.dp)
                                )
                            }
                        } else {
                            items(state.visibleItems) { item ->
                                ItemCard(
                                    item = item,
                                    onItemClick = onNavigateToDetail,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("¿Qué herramienta necesitas hoy?") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoriesRow(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategoryClick: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(categories) { category ->
            val selected = category.id == selectedCategoryId
            FilterChip(
                selected = selected,
                onClick = { onCategoryClick(category.id) },
                label = { Text(category.name) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(50)
            )
        }
    }
}
