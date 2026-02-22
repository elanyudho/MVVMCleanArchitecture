package com.elanyudho.feature.home.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elanyudho.core.base.presentation.viewmodel.BaseViewModel
import com.elanyudho.core.base.presentation.state.UiEvent
import com.elanyudho.core.base.data.pagination.loadPage
import com.elanyudho.feature.home.domain.usecase.GetProductsUseCase
import com.elanyudho.feature.home.domain.usecase.GetUsersUseCase
import com.elanyudho.feature.home.presentation.state.ExploreUiState

class ExploreViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<ExploreUiState, UiEvent>(ExploreUiState()) {

    init {
        loadMoreFeatured()
        loadMorePopular()
        loadMoreUsers()
    }

    // ───── Featured Products (horizontal) ─────

    fun loadMoreFeatured() {
        loadPage(
            scope = viewModelScope,
            currentState = currentState.featured,
            updateState = { updateState { copy(featured = it, isLoading = false) } },
            fetchPage = { page, size ->
                getProductsUseCase(GetProductsUseCase.Params("featured", page, size))
            }
        )
    }

    // ───── Popular Products (horizontal) ─────

    fun loadMorePopular() {
        loadPage(
            scope = viewModelScope,
            currentState = currentState.popular,
            updateState = { updateState { copy(popular = it, isLoading = false) } },
            fetchPage = { page, size ->
                getProductsUseCase(GetProductsUseCase.Params("popular", page, size))
            }
        )
    }

    // ───── Recent Users (vertical) ─────

    fun loadMoreUsers() {
        loadPage(
            scope = viewModelScope,
            currentState = currentState.recentUsers,
            updateState = { updateState { copy(recentUsers = it, isLoading = false) } },
            fetchPage = { page, size ->
                getUsersUseCase(GetUsersUseCase.Params(page, size))
            }
        )
    }
}
