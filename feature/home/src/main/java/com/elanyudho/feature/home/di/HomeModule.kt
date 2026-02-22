package com.elanyudho.feature.home.di

import com.elanyudho.feature.home.data.repository.FakeProductRepository
import com.elanyudho.feature.home.data.repository.FakeUserRepository
import com.elanyudho.feature.home.domain.repository.ProductRepository
import com.elanyudho.feature.home.domain.repository.UserRepository
import com.elanyudho.feature.home.domain.usecase.GetProductsUseCase
import com.elanyudho.feature.home.domain.usecase.GetUsersUseCase
import com.elanyudho.feature.home.presentation.viewmodel.ExploreViewModel
import com.elanyudho.feature.home.presentation.viewmodel.UserListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for Home feature.
 *
 * Currently uses Fake repositories for demo.
 * To switch to real API, uncomment the "REAL API" section
 * and comment out the "FAKE" section below.
 */
val homeModule = module {

    // ==================== Data Layer ====================

    // --- FAKE (for demo / development) ---
    single<UserRepository> { FakeUserRepository() }
    single<ProductRepository> { FakeProductRepository() }

    // --- REAL API (uncomment when API is ready) ---
    // single { HomeRemoteDataSource(httpClient = get()) }
    // single<UserRepository> { UserRepositoryImpl(remoteDataSource = get()) }
    // single<ProductRepository> { ProductRepositoryImpl(remoteDataSource = get()) }

    // ==================== Domain Layer ====================

    factory { GetUsersUseCase(userRepository = get()) }
    factory { GetProductsUseCase(productRepository = get()) }

    // ==================== Presentation Layer ====================

    viewModelOf(::UserListViewModel)
    viewModelOf(::ExploreViewModel)
}
