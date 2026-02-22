package com.elanyudho.core.base.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Base response wrapper for paginated API responses.
 */
@Serializable
data class BasePagingResponse<DATA>(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<DATA>? = null,
    @SerialName("errors")
    val errors: List<ErrorModel>? = null,
    @SerialName("metadata")
    val metadata: MetaDataModel? = null
)
