package com.elanyudho.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.elanyudho.core.base.data.pagination.PaginationState

// ═══════════════════════════════════════════════════════════════
//  VERTICAL — LazyListScope extension
//  Use inside any LazyColumn (single list or multi-section)
// ═══════════════════════════════════════════════════════════════

/**
 * Adds paginated items to a [LazyColumn][androidx.compose.foundation.lazy.LazyColumn].
 * Handles: item rendering, load-more detection, loading indicator, and error retry.
 *
 * Works identically for:
 * - Single list screens
 * - Multi-section screens (call multiple times in one LazyColumn)
 *
 * Usage:
 * ```
 * LazyColumn {
 *     paginatedColumnItems(
 *         state = uiState.pagination,
 *         onLoadMore = viewModel::loadMore,
 *         key = { it.id }
 *     ) { user -> UserCard(user) }
 * }
 * ```
 */
fun <T> LazyListScope.paginatedColumnItems(
    state: PaginationState<T>,
    onLoadMore: () -> Unit,
    threshold: Int = 3,
    key: ((T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    // Render items
    items(
        count = state.items.size,
        key = key?.let { keyFn -> { index -> keyFn(state.items[index]) } }
    ) { index ->
        itemContent(state.items[index])

        // Trigger load more when within threshold of the end
        if (index >= state.items.size - threshold && state.canLoadMore) {
            LaunchedEffect(state.currentPage) {
                onLoadMore()
            }
        }
    }

    // Loading more indicator
    if (state.isLoadingMore) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }

    // Error retry for load-more failures (when data already exists)
    if (state.error != null && state.items.isNotEmpty()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Failed to load more",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onLoadMore) {
                    Text("Retry")
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  HORIZONTAL — @Composable function
//  Same API pattern as paginatedColumnItems, different axis
// ═══════════════════════════════════════════════════════════════

/**
 * Horizontal paginated list using [LazyRow].
 * Same parameter pattern as [paginatedColumnItems] — accepts [PaginationState].
 *
 * Usage:
 * ```
 * paginatedRowItems(
 *     state = uiState.trending,
 *     onLoadMore = viewModel::loadMoreTrending,
 *     key = { it.id }
 * ) { movie -> MovieCard(movie) }
 * ```
 */
@Composable
fun <T> PaginatedRowItems(
    state: PaginationState<T>,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
    threshold: Int = 2,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    key: ((T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        items(
            count = state.items.size,
            key = key?.let { keyFn -> { index -> keyFn(state.items[index]) } }
        ) { index ->
            itemContent(state.items[index])

            // Trigger load more when within threshold of the end
            if (index >= state.items.size - threshold && state.canLoadMore) {
                LaunchedEffect(state.currentPage) {
                    onLoadMore()
                }
            }
        }

        // Loading more indicator
        if (state.isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillParentMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
