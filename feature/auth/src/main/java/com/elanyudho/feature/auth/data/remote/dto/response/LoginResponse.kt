package com.elanyudho.feature.auth.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from login/register API.
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
