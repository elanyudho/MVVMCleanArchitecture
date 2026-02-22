package com.elanyudho.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import com.elanyudho.core.base.data.wrapper.AppError

/**
 * Reusable screen wrapper that handles common screen states:
 * - **Loading**: Shows shimmer or spinner (configurable via [loadingType])
 * - **Error**: Full-screen error with retry button (when no data)
 * - **Empty**: Empty state with icon, title, subtitle
 * - **Content**: Your actual screen content
 * - **Pull-to-Refresh**: Optional, wraps content with [PullToRefreshBox]
 * - **Snackbar**: Auto-shows on error when data is present (non-blocking)
 *
 * State priority: Loading > Error (no data) > Empty > Content
 *
 * Usage:
 * ```
 * ScreenContainer(
 *     isLoading = uiState.isLoading,
 *     error = uiState.error,
 *     isEmpty = items.isEmpty(),
 *     enablePullToRefresh = true,
 *     onRefresh = viewModel::refresh,
 *     onRetry = viewModel::retry
 * ) {
 *     LazyColumn { ... }
 * }
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isRefreshing: Boolean = false,
    error: AppError? = null,
    isEmpty: Boolean = false,
    enablePullToRefresh: Boolean = false,
    onRefresh: () -> Unit = {},
    onRetry: () -> Unit = {},
    onDismissError: () -> Unit = {},
    loadingType: LoadingType = LoadingType.Spinner,
    emptyTitle: String = "No data available",
    emptySubtitle: String? = null,
    emptyIcon: ImageVector = Icons.Outlined.Info,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable () -> Unit
) {
    // Auto-show snackbar for non-blocking errors (when data exists)
    LaunchedEffect(error) {
        error?.let {
            val result = snackbarHostState.showSnackbar(
                message = it.message,
                actionLabel = if (it.isRetryable()) "Retry" else null,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRetry()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when {
            // Initial loading (no data yet)
            isLoading && !isRefreshing -> {
                LoadingContent(
                    loadingType = loadingType,
                    modifier = contentModifier
                )
            }

            // Error with no data → full-screen error
            error != null && isEmpty -> {
                ErrorContent(
                    error = error,
                    onRetry = onRetry,
                    onDismiss = onDismissError,
                    modifier = contentModifier
                )
            }

            // Empty state (not loading, no error)
            isEmpty && !isLoading -> {
                EmptyContent(
                    title = emptyTitle,
                    subtitle = emptySubtitle,
                    icon = emptyIcon,
                    modifier = contentModifier
                )
            }

            // Content (with optional pull-to-refresh)
            else -> {
                if (enablePullToRefresh) {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        modifier = contentModifier
                    ) {
                        content()
                    }
                } else {
                    Box(modifier = contentModifier) {
                        content()
                    }
                }
            }
        }
    }
}
