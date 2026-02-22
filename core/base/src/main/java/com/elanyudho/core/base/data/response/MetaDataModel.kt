package com.elanyudho.core.base.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Metadata container from paginated API responses.
 */
@Serializable
data class MetaDataModel(
    @SerialName("pagination")
    val pagination: PaginationModel
)

/**
 * Pagination details from API response metadata.
 */
@Serializable
data class PaginationModel(
    @SerialName("limit")
    val limit: Int,
    @SerialName("next_page")
    val nextPage: Boolean,
    @SerialName("page")
    val page: Int,
    @SerialName("prev_page")
    val prevPage: Boolean,
    @SerialName("total")
    val total: Int
)
