package com.elanyudho.feature.home.data.remote

import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.core.base.data.response.BasePagingResponse
import com.elanyudho.core.network.safeApiCall
import com.elanyudho.feature.home.data.remote.dto.response.ProductResponse
import com.elanyudho.feature.home.data.remote.dto.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Remote data source for Home feature API calls.
 * Uses [safeApiCall] to wrap Ktor calls with error handling.
 *
 * All paginated responses use [BasePagingResponse] from core:network.
 */
class HomeRemoteDataSource(
    private val httpClient: HttpClient
) {

    companion object {
        private const val USERS_ENDPOINT = "users"
        private const val PRODUCTS_ENDPOINT = "products"
    }

    /**
     * GET /users?page=1&size=20
     */
    suspend fun getUsers(
        page: Int,
        pageSize: Int
    ): Result<BasePagingResponse<UserResponse>> = safeApiCall {
        httpClient.get(USERS_ENDPOINT) {
            parameter("page", page)
            parameter("size", pageSize)
        }.body()
    }

    /**
     * GET /products?category=featured&page=1&size=10
     */
    suspend fun getProducts(
        category: String,
        page: Int,
        pageSize: Int
    ): Result<BasePagingResponse<ProductResponse>> = safeApiCall {
        httpClient.get(PRODUCTS_ENDPOINT) {
            parameter("category", category)
            parameter("page", page)
            parameter("size", pageSize)
        }.body()
    }
}
