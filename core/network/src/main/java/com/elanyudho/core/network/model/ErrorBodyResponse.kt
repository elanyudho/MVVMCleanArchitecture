package com.elanyudho.core.network.model

import com.elanyudho.core.base.data.response.ErrorModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Lightweight model for parsing API error response bodies.
 *
 * Used internally by SafeApiCall to extract error info
 * without requiring non-null `data` field like [BaseResponse].
 */
@Serializable
internal data class ErrorBodyResponse(
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("errors")
    val errors: List<ErrorModel>? = null
)
