package com.elanyudho.core.base.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base response wrapper for single-item API responses.
 */
@Serializable
data class BaseResponse<DATA>(
    @SerialName("code")
    val code: String? = null,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: DATA,
    @SerialName("errors")
    val errors: List<ErrorModel>? = null
)
