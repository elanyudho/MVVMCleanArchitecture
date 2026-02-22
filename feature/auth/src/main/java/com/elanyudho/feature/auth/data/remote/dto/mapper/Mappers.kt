package com.elanyudho.feature.auth.data.remote.dto.mapper

import com.elanyudho.feature.auth.domain.model.User
import com.elanyudho.feature.auth.data.remote.dto.response.LoginResponse

/**
 * Mapper extension to convert LoginResponse DTO to User domain model.
 */
fun LoginResponse.toUser(): User = User(
    id = id,
    email = email,
    name = name,
    avatarUrl = avatarUrl,
    token = token
)
