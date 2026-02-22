package com.elanyudho.feature.home.data.remote.dto.mapper

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.response.BasePagingResponse
import com.elanyudho.feature.home.data.remote.dto.response.ProductResponse
import com.elanyudho.feature.home.data.remote.dto.response.UserResponse
import com.elanyudho.feature.home.domain.model.Product
import com.elanyudho.feature.home.domain.model.User

// ==================== User Mappers ====================

fun UserResponse.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    avatarUrl = avatarUrl ?: ""
)

@JvmName("userResponseToPagedResult")
fun BasePagingResponse<UserResponse>.toPagedResult(): PagedResult<User> {
    val pagination = metadata?.pagination
    return PagedResult(
        items = data?.map { it.toDomain() } ?: emptyList(),
        currentPage = pagination?.page ?: 0,
        totalPages = pagination?.let { if (it.nextPage) it.page + 1 else it.page },
        totalItems = pagination?.total
    )
}

// ==================== Product Mappers ====================

fun ProductResponse.toDomain(): Product = Product(
    id = id,
    name = name,
    price = price,
    category = category
)

@JvmName("productResponseToPagedResult")
fun BasePagingResponse<ProductResponse>.toPagedResult(): PagedResult<Product> {
    val pagination = metadata?.pagination
    return PagedResult(
        items = data?.map { it.toDomain() } ?: emptyList(),
        currentPage = pagination?.page ?: 0,
        totalPages = pagination?.let { if (it.nextPage) it.page + 1 else it.page },
        totalItems = pagination?.total
    )
}
