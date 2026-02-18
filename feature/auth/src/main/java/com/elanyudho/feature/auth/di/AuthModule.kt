package com.elanyudho.feature.auth.di

import com.elanyudho.feature.auth.data.local.AuthLocalDataSource
import com.elanyudho.feature.auth.data.remote.AuthRemoteDataSource
import com.elanyudho.feature.auth.data.repository.AuthRepositoryImpl
import com.elanyudho.feature.auth.domain.repository.AuthRepository
import com.elanyudho.feature.auth.domain.usecase.LoginUseCase
import com.elanyudho.feature.auth.domain.usecase.ObserveCurrentUserUseCase
import com.elanyudho.feature.auth.presentation.viewmodel.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for Auth feature.
 * Includes Data, Domain, and Presentation layer bindings.
 */
val authModule = module {
    
    // ==================== Data Layer ====================
    
    single { AuthRemoteDataSource(httpClient = get()) }
    
    single { AuthLocalDataSource(userDao = get(), securePrefs = get()) }
    
    single<AuthRepository> { 
        AuthRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get(),
            connectivityMonitor = get()
        )
    }
    
    // ==================== Domain Layer ====================
    
    // ONE-SHOT UseCase
    factory { LoginUseCase(authRepository = get()) }
    
    // FLOW UseCase
    factory { ObserveCurrentUserUseCase(authRepository = get()) }
    
    // ==================== Presentation Layer ====================
    
    viewModelOf(::LoginViewModel)
}
