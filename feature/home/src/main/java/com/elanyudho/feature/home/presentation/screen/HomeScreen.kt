package com.elanyudho.feature.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.elanyudho.core.ui.component.LoadingType
import com.elanyudho.core.ui.component.ScreenContainer
import com.elanyudho.core.ui.component.paginatedColumnItems
import com.elanyudho.feature.home.domain.model.User
import com.elanyudho.feature.home.presentation.viewmodel.UserListViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Home screen displaying a paginated list of users.
 * Demonstrates: ScreenContainer + paginatedColumnItems + loadPage.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToExplore: () -> Unit = {},
    viewModel: UserListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagination = uiState.pagination

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users") },
                actions = {
                    IconButton(onClick = onNavigateToExplore) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Explore"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContainer(
            modifier = Modifier.padding(innerPadding),
            isLoading = uiState.isLoading && pagination.items.isEmpty(),
            isRefreshing = uiState.isRefreshing,
            error = uiState.error,
            isEmpty = pagination.isEmpty,
            enablePullToRefresh = true,
            onRefresh = viewModel::refresh,
            onRetry = viewModel::retry,
            loadingType = LoadingType.Shimmer,
            emptyTitle = "No users found",
            emptySubtitle = "Pull down to refresh"
        ) {
            LazyColumn {
                paginatedColumnItems(
                    state = pagination,
                    onLoadMore = viewModel::loadNextPage,
                    key = { it.id }
                ) { user ->
                    UserCard(user = user)
                }
            }
        }
    }
}

/**
 * Individual user card displayed in the list.
 */
@Composable
private fun UserCard(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder (colored circle with initial)
            AvatarPlaceholder(
                name = user.name,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleSmall,
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
}

/**
 * Simple avatar placeholder showing the first letter of the name.
 */
@Composable
private fun AvatarPlaceholder(
    name: String,
    modifier: Modifier = Modifier
) {
    val initial = name.firstOrNull()?.uppercase() ?: "?"

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clip(CircleShape)
            .size(48.dp)
            .then(
                Modifier.padding(0.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = androidx.compose.ui.graphics.Color(0xFF6750A4)
            )
        }
        Text(
            text = initial,
            style = MaterialTheme.typography.titleMedium,
            color = androidx.compose.ui.graphics.Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
