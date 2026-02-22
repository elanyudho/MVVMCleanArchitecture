package com.elanyudho.feature.home.domain.usecase

import com.elanyudho.core.base.presentation.usecase.UseCase
import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.domain.model.User
import com.elanyudho.feature.home.domain.repository.UserRepository

/**
 * Fetches a paginated list of users.
 */
class GetUsersUseCase(
    private val userRepository: UserRepository
) : UseCase<GetUsersUseCase.Params, PagedResult<User>>() {

    data class Params(val page: Int, val pageSize: Int)

    override suspend fun invoke(params: Params): Result<PagedResult<User>> {
        return userRepository.getUsers(page = params.page, pageSize = params.pageSize)
    }
}
