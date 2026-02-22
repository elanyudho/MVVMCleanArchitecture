package com.elanyudho.feature.home.data.repository

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.domain.model.User
import com.elanyudho.feature.home.domain.repository.UserRepository
import kotlinx.coroutines.delay

/**
 * Fake implementation of [UserRepository] for demonstration.
 * Simulates network delay and returns generated user data.
 *
 * Replace this with a real API-backed implementation:
 * ```
 * class UserRepositoryImpl(
 *     private val httpClient: HttpClient
 * ) : UserRepository {
 *     override suspend fun getUsers(page: Int, pageSize: Int): Result<PagedResult<User>> {
 *         return safeApiCall {
 *             httpClient.get("users") { parameter("page", page); parameter("size", pageSize) }
 *         }
 *     }
 * }
 * ```
 */
class FakeUserRepository : UserRepository {

    companion object {
        private const val TOTAL_USERS = 75
        private const val SIMULATED_DELAY_MS = 800L
    }

    override suspend fun getUsers(page: Int, pageSize: Int): Result<PagedResult<User>> {
        // Simulate network delay
        delay(SIMULATED_DELAY_MS)

        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, TOTAL_USERS)

        if (startIndex >= TOTAL_USERS) {
            return Result.Success(
                PagedResult(
                    items = emptyList(),
                    currentPage = page,
                    totalPages = (TOTAL_USERS + pageSize - 1) / pageSize
                )
            )
        }

        val users = (startIndex until endIndex).map { index ->
            User(
                id = index + 1,
                name = "User ${index + 1}",
                email = "user${index + 1}@example.com",
                avatarUrl = "https://i.pravatar.cc/150?img=${(index % 70) + 1}"
            )
        }

        val totalPages = (TOTAL_USERS + pageSize - 1) / pageSize

        return Result.Success(
            PagedResult(
                items = users,
                currentPage = page,
                totalPages = totalPages,
                totalItems = TOTAL_USERS
            )
        )
    }
}
