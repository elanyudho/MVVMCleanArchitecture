package com.elanyudho.core.base.data.pagination

import com.elanyudho.core.base.data.wrapper.AppError

/**
 * Immutable pagination state. Lives inside your UiState.
 *
 * @param T The type of items being paginated
 * @param items Accumulated items across all loaded pages
 * @param currentPage Current page number (0 = no pages loaded yet)
 * @param pageSize Number of items per page
 * @param isLoadingMore True while loading the next page
 * @param hasReachedEnd True when no more pages to load
 * @param error Error from the last page load attempt
 */
data class PaginationState<T>(
    val items: List<T> = emptyList(),
    val currentPage: Int = 0,
    val pageSize: Int = 20,
    val isLoadingMore: Boolean = false,
    val hasReachedEnd: Boolean = false,
    val error: AppError? = null
) {
    /** True when there are no items and not currently loading */
    val isEmpty: Boolean get() = items.isEmpty() && !isLoadingMore

    /** The next page number to request */
    val nextPage: Int get() = currentPage + 1

    /** True when it's safe to request more data */
    val canLoadMore: Boolean get() = !isLoadingMore && !hasReachedEnd && error == null

    /** Reset to initial state (keeps pageSize) */
    fun reset(): PaginationState<T> = PaginationState(pageSize = pageSize)
}
