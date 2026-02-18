package com.elanyudho.feature.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO for login request.
 */
@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

/**
 * DTO for login response.
 */
@Serializable
data class LoginResponse(
    @SerialName("id")
    val id: String,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("token")
    val token: String,
    @SerialName("refresh_token")
    val refreshToken: String? = null
)

/**
 * DTO for register request.
 */
@Serializable
data class RegisterRequest(
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

/**
 * DTO for generic API response wrapper.
 */
@Serializable
data class ApiResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String? = null,
    @SerialName("data")
    val data: T? = null,
    @SerialName("errors")
    val errors: Map<String, List<String>>? = null
)

/**
 * DTO for refresh token request.
 */
@Serializable
data class RefreshTokenRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)

/**
 * DTO for refresh token response.
 */
@Serializable
data class RefreshTokenResponse(
    @SerialName("token")
    val token: String,
    @SerialName("refresh_token")
    val refreshToken: String?
)
