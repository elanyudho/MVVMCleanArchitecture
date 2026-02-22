package com.elanyudho.feature.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.base.presentation.viewmodel.BaseViewModel
import com.elanyudho.core.base.presentation.state.UiEvent
import com.elanyudho.core.base.data.pagination.loadPage
import com.elanyudho.feature.home.domain.usecase.GetUsersUseCase
import com.elanyudho.feature.home.presentation.state.HomeUiState

class UserListViewModel(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<HomeUiState, UiEvent>(HomeUiState()) {

    init {
        loadNextPage()
    }

    /**
     * Load the next page of users.
     * Safe to call multiple times — [loadPage] guards against concurrent/duplicate loads.
     */
    fun loadNextPage() {
        loadPage(
            scope = viewModelScope,
            currentState = currentState.pagination,
            updateState = { newPaginationState ->
                updateState {
                    copy(
                        pagination = newPaginationState,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            },
            fetchPage = { page, size ->
                getUsersUseCase(GetUsersUseCase.Params(page = page, pageSize = size))
            }
        )
    }

    /**
     * Pull-to-refresh: reset pagination and reload from page 1.
     */
    fun refresh() {
        updateState {
            copy(
                isRefreshing = true,
                pagination = pagination.reset()
            )
        }
        loadNextPage()
    }

    /**
     * Retry after a failed page load.
     */
    fun retry() {
        updateState { copy(pagination = pagination.copy(error = null)) }
        loadNextPage()
    }
}
