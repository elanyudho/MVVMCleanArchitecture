package com.elanyudho.feature.auth.data.remote

import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.core.base.data.response.BaseResponse
import com.elanyudho.core.network.safeApiCall
import com.elanyudho.feature.auth.data.remote.dto.request.LoginRequest
import com.elanyudho.feature.auth.data.remote.dto.request.RegisterRequest
import com.elanyudho.feature.auth.data.remote.dto.response.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

/**
 * Remote data source for authentication API calls.
 *
 * All responses wrapped in [BaseResponse] which contains
 * code, message, data, and optional errors.
 */
class AuthRemoteDataSource(
    private val httpClient: HttpClient
) {
    
    companion object {
        private const val LOGIN_ENDPOINT = "auth/login"
        private const val REGISTER_ENDPOINT = "auth/register"
        private const val LOGOUT_ENDPOINT = "auth/logout"
        private const val REFRESH_TOKEN_ENDPOINT = "auth/refresh"
        private const val ME_ENDPOINT = "auth/me"
    }
    
    /**
     * Performs login API call.
     */
    suspend fun login(email: String, password: String): Result<BaseResponse<LoginResponse>> = safeApiCall {
        httpClient.post(LOGIN_ENDPOINT) {
            setBody(LoginRequest(email = email, password = password))
        }.body()
    }
    
    /**
     * Performs register API call.
     */
    suspend fun register(
        name: String, 
        email: String, 
        password: String
    ): Result<BaseResponse<LoginResponse>> = safeApiCall {
        httpClient.post(REGISTER_ENDPOINT) {
            setBody(RegisterRequest(name = name, email = email, password = password))
        }.body()
    }
    
    /**
     * Performs logout API call.
     */
    suspend fun logout(): Result<Unit> = safeApiCall {
        httpClient.post(LOGOUT_ENDPOINT).body()
    }
    
    /**
     * Gets current user profile.
     */
    suspend fun getCurrentUser(): Result<BaseResponse<LoginResponse>> = safeApiCall {
        httpClient.get(ME_ENDPOINT).body()
    }
    
    /**
     * Refreshes authentication token.
     */
    suspend fun refreshToken(refreshToken: String): Result<BaseResponse<LoginResponse>> = safeApiCall {
        httpClient.post(REFRESH_TOKEN_ENDPOINT) {
            setBody(mapOf("refresh_token" to refreshToken))
        }.body()
    }
}
