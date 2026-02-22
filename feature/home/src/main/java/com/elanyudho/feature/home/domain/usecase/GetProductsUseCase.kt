package com.elanyudho.feature.home.domain.usecase

import com.elanyudho.core.base.presentation.usecase.UseCase
import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.domain.model.Product
import com.elanyudho.feature.home.domain.repository.ProductRepository

/**
 * Fetches a paginated list of products by category.
 */
class GetProductsUseCase(
    private val productRepository: ProductRepository
) : UseCase<GetProductsUseCase.Params, PagedResult<Product>>() {

    data class Params(val category: String, val page: Int, val pageSize: Int)

    override suspend fun invoke(params: Params): Result<PagedResult<Product>> {
        return productRepository.getProducts(
            category = params.category,
            page = params.page,
            pageSize = params.pageSize
        )
    }
}
