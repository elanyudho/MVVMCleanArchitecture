package com.elanyudho.feature.auth.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from refresh token API.
 */
@Serializable
data class RefreshTokenResponse(
    @SerialName("token")
    val token: String,
    @SerialName("refresh_token")
    val refreshToken: String?
)
