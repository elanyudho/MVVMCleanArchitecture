package com.elanyudho.feature.home.data.repository

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.domain.model.Product
import com.elanyudho.feature.home.domain.repository.ProductRepository
import kotlinx.coroutines.delay

/**
 * Fake implementation of [ProductRepository] for demonstration.
 * Generates products per category with simulated network delay.
 */
class FakeProductRepository : ProductRepository {

    companion object {
        private const val TOTAL_PER_CATEGORY = 30
        private const val SIMULATED_DELAY_MS = 600L
    }

    override suspend fun getProducts(
        category: String,
        page: Int,
        pageSize: Int
    ): Result<PagedResult<Product>> {
        delay(SIMULATED_DELAY_MS)

        val startIndex = (page - 1) * pageSize
        val endIndex = minOf(startIndex + pageSize, TOTAL_PER_CATEGORY)

        if (startIndex >= TOTAL_PER_CATEGORY) {
            return Result.Success(
                PagedResult(
                    items = emptyList(),
                    currentPage = page,
                    totalPages = (TOTAL_PER_CATEGORY + pageSize - 1) / pageSize
                )
            )
        }

        val products = (startIndex until endIndex).map { index ->
            Product(
                id = category.hashCode() + index + 1,
                name = "${category.replaceFirstChar { it.uppercase() }} Item ${index + 1}",
                price = "$${(index + 1) * 10 + (index % 5) * 5}.99",
                category = category
            )
        }

        val totalPages = (TOTAL_PER_CATEGORY + pageSize - 1) / pageSize

        return Result.Success(
            PagedResult(
                items = products,
                currentPage = page,
                totalPages = totalPages,
                totalItems = TOTAL_PER_CATEGORY
            )
        )
    }
}
