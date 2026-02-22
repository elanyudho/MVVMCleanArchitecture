package com.elanyudho.core.base.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



/**
 * Error detail from API response.
 */
@Serializable
data class ErrorModel(
    @SerialName("field")
    val field: String? = null,
    @SerialName("message")
    val message: String
)
