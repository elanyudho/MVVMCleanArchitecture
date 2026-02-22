package com.elanyudho.feature.home.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Product response DTO from API.
 */
@Serializable
data class ProductResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("price")
    val price: String,
    @SerialName("category")
    val category: String
)
