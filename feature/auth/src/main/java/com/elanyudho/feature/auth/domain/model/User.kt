package com.elanyudho.feature.auth.domain.model

import kotlinx.serialization.Serializable

/**
 * User entity representing authenticated user data.
 */
@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String? = null,
    val token: String? = null
)
