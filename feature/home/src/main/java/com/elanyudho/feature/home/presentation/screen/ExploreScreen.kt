package com.elanyudho.feature.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elanyudho.core.ui.component.paginatedColumnItems
import com.elanyudho.core.ui.component.PaginatedRowItems
import com.elanyudho.feature.home.domain.model.Product
import com.elanyudho.feature.home.domain.model.User
import com.elanyudho.feature.home.presentation.viewmodel.ExploreViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * ExploreScreen — demonstrates multiple paginated lists in one screen:
 * 1. Featured Products → horizontal (PaginatedRowItems)
 * 2. Popular Products  → horizontal (PaginatedRowItems)
 * 3. Recent Users      → vertical   (paginatedColumnItems)
 *
 * All 3 sections are independent — each has its own PaginationState,
 * loads pages independently, and shows its own loading/error indicators.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Explore") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            // ───── Section 1: Featured (horizontal) ─────
            item {
                SectionHeader(title = "⭐ Featured")
            }
            item {
                PaginatedRowItems(
                    state = state.featured,
                    onLoadMore = viewModel::loadMoreFeatured,
                    key = { it.id }
                ) { product ->
                    ProductCard(product = product)
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            // ───── Section 2: Popular (horizontal) ─────
            item {
                SectionHeader(title = "🔥 Popular")
            }
            item {
                PaginatedRowItems(
                    state = state.popular,
                    onLoadMore = viewModel::loadMorePopular,
                    key = { it.id }
                ) { product ->
                    ProductCard(product = product)
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            // ───── Section 3: Recent Users (vertical) ─────
            item {
                SectionHeader(title = "👥 Recent Users")
            }
            paginatedColumnItems(
                state = state.recentUsers,
                onLoadMore = viewModel::loadMoreUsers,
                key = { it.id }
            ) { user ->
                UserRow(user = user)
            }
        }
    }
}

// ==================== Sub-Components ====================

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = product.price,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun UserRow(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(color = androidx.compose.ui.graphics.Color(0xFF6750A4))
            }
            Text(
                text = user.name.firstOrNull()?.uppercase() ?: "?",
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
