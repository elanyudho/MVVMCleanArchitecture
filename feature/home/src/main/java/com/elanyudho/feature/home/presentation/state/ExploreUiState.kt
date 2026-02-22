package com.elanyudho.feature.home.presentation.state

import com.elanyudho.core.base.presentation.state.UiState
import com.elanyudho.core.base.data.pagination.PaginationState
import com.elanyudho.core.base.data.wrapper.AppError
import com.elanyudho.feature.home.domain.model.Product
import com.elanyudho.feature.home.domain.model.User

/**
 * State for ExploreScreen — demonstrates multiple PaginationState in one screen.
 *
 * Each section has its own independent pagination:
 * - [featured] → horizontal products (pageSize = 8)
 * - [popular] → horizontal products (pageSize = 10)
 * - [recentUsers] → vertical users (pageSize = 15)
 */
data class ExploreUiState(
    override val isLoading: Boolean = true,
    override val error: AppError? = null,
    val featured: PaginationState<Product> = PaginationState(pageSize = 8),
    val popular: PaginationState<Product> = PaginationState(pageSize = 10),
    val recentUsers: PaginationState<User> = PaginationState(pageSize = 15)
) : UiState
