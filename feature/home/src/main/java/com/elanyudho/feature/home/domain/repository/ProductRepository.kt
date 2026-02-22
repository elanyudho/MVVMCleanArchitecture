package com.elanyudho.feature.home.domain.repository

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.domain.model.Product

/**
 * Repository contract for Product data.
 */
interface ProductRepository {

    /**
     * Fetches a page of products by category.
     */
    suspend fun getProducts(category: String, page: Int, pageSize: Int): Result<PagedResult<Product>>
}
