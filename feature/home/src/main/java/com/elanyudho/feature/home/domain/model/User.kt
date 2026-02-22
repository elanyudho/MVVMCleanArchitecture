package com.elanyudho.feature.home.domain.model

/**
 * Domain model representing a User.
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String
)
