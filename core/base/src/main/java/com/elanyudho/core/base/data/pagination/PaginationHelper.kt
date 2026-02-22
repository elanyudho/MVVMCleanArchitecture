package com.elanyudho.core.base.data.pagination

import com.elanyudho.core.base.data.wrapper.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Loads the next page of data.
 *
 * This is a standalone function (not a BaseViewModel extension) so it works
 * with any CoroutineScope, making it testable without a ViewModel.
 *
 * Usage in ViewModel:
 * ```
 * fun loadNextPage() {
 *     loadPage(
 *         scope = viewModelScope,
 *         currentState = currentState.pagination,
 *         updateState = { updateState { copy(pagination = it) } },
 *         fetchPage = { page, size -> useCase(Params(page, size)) }
 *     )
 * }
 * ```
 *
 * @param T The type of items being paginated
 * @param scope CoroutineScope for launching the load operation
 * @param currentState The current [PaginationState]
 * @param updateState Callback to update the pagination state in UiState
 * @param fetchPage Suspend function that fetches a page of data
 */
fun <T> loadPage(
    scope: CoroutineScope,
    currentState: PaginationState<T>,
    updateState: (PaginationState<T>) -> Unit,
    fetchPage: suspend (page: Int, pageSize: Int) -> Result<PagedResult<T>>
) {
    if (!currentState.canLoadMore) return

    scope.launch {
        // Set loading state
        updateState(currentState.copy(isLoadingMore = true, error = null))

        when (val result = fetchPage(currentState.nextPage, currentState.pageSize)) {
            is Result.Success -> {
                val data = result.data
                updateState(
                    currentState.copy(
                        items = currentState.items + data.items,
                        currentPage = data.currentPage,
                        isLoadingMore = false,
                        hasReachedEnd = !data.hasMorePages(currentState.pageSize)
                    )
                )
            }

            is Result.Error -> {
                updateState(
                    currentState.copy(
                        isLoadingMore = false,
                        error = result.error
                    )
                )
            }

            is Result.Loading -> { /* Handled by isLoadingMore flag */ }
        }
    }
}
