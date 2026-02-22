package com.elanyudho.feature.home.presentation.state

import com.elanyudho.core.base.presentation.state.UiState
import com.elanyudho.core.base.data.pagination.PaginationState
import com.elanyudho.core.base.data.wrapper.AppError
import com.elanyudho.feature.home.domain.model.User

data class HomeUiState(
    override val isLoading: Boolean = true,
    override val error: AppError? = null,
    val isRefreshing: Boolean = false,
    val pagination: PaginationState<User> = PaginationState(pageSize = 20)
) : UiState
