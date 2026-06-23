package com.prezto.prezto.feature_explore.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.prezto.prezto.core.designsystem.PreztoTheme
import com.prezto.prezto.core.designsystem.TrustBadge
import com.prezto.prezto.core.utils.toSoles
import com.prezto.prezto.feature_explore.domain.model.Item
import com.prezto.prezto.feature_explore.domain.model.ItemCondition
import com.prezto.prezto.core.domain.model.TrustScore
import com.prezto.prezto.feature_explore.domain.model.Owner

@Composable
fun ItemCard(
    item: Item,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(item.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                // Badge de confianza del propietario, anclado sobre la imagen.
                TrustBadge(
                    trustScore = item.owner.trustScore,
                    onDark = true,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Por ${item.owner.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                val conditionColor = when (item.currentCondition) {
                    ItemCondition.NEW -> MaterialTheme.colorScheme.primary
                    ItemCondition.GOOD -> MaterialTheme.colorScheme.secondary
                    ItemCondition.FAIR -> MaterialTheme.colorScheme.error
                    ItemCondition.NEEDS_REPAIR -> MaterialTheme.colorScheme.errorContainer
                }
                Text(
                    text = item.currentCondition.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = conditionColor,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tarifa Diaria",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = item.dailyRate.toSoles(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun ItemCardPreview() {
    PreztoTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ItemCard(
                item = Item(
                    id = "1",
                    categoryId = "cat1",
                    title = "Taladro Percutor Bosch 750W",
                    description = "Lorem ipsum description...",
                    dailyRate = 25.0,
                    hourlyRate = 5.0,
                    currentCondition = ItemCondition.GOOD,
                    isAvailable = true,
                    imageUrl = "",
                    owner = Owner("u1", "Rodrigo A.", TrustScore(4.9), isVerified = true)
                ),
                onItemClick = {}
            )
        }
    }
}
