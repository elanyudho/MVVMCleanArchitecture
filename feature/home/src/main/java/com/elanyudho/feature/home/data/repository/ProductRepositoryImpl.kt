package com.elanyudho.feature.home.data.repository

import com.elanyudho.core.base.data.pagination.PagedResult
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.feature.home.data.remote.HomeRemoteDataSource
import com.elanyudho.feature.home.data.remote.dto.mapper.toPagedResult
import com.elanyudho.feature.home.domain.model.Product
import com.elanyudho.feature.home.domain.repository.ProductRepository

/**
 * Real implementation of [ProductRepository].
 * 
 * Flow: API → DTO → Mapper → Domain Model
 */
class ProductRepositoryImpl(
    private val remoteDataSource: HomeRemoteDataSource
) : ProductRepository {

    override suspend fun getProducts(
        category: String,
        page: Int,
        pageSize: Int
    ): Result<PagedResult<Product>> {
        return when (val result = remoteDataSource.getProducts(category, page, pageSize)) {
            is Result.Success -> Result.Success(result.data.toPagedResult())
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
}
