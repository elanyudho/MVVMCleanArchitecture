package com.elanyudho.feature.home.domain.repository

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.domain.model.User

/**
 * Repository contract for User data.
 */
interface UserRepository {

    /**
     * Fetches a page of users.
     *
     * @param page Page number (1-based)
     * @param pageSize Number of items per page
     * @return [Result] wrapping a [PagedResult] of [User]
     */
    suspend fun getUsers(page: Int, pageSize: Int): Result<PagedResult<User>>
}
