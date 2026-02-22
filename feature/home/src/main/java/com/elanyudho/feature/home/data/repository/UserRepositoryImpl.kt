package com.elanyudho.feature.home.data.repository

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.data.remote.HomeRemoteDataSource
import com.elanyudho.feature.home.data.remote.dto.mapper.toPagedResult
import com.elanyudho.feature.home.domain.model.User
import com.elanyudho.feature.home.domain.repository.UserRepository

/**
 * Real implementation of [UserRepository].
 * 
 * Flow: API → DTO → Mapper → Domain Model
 * Error handling is done by [safeApiCall] in RemoteDataSource.
 */
class UserRepositoryImpl(
    private val remoteDataSource: HomeRemoteDataSource
) : UserRepository {

    override suspend fun getUsers(page: Int, pageSize: Int): Result<PagedResult<User>> {
        return when (val result = remoteDataSource.getUsers(page, pageSize)) {
            is Result.Success -> Result.Success(result.data.toPagedResult())
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}
