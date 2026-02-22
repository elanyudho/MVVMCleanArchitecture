package com.elanyudho.core.base.presentation.usecase

import com.elanyudho.core.base.data.wrapper.Result
import kotlinx.coroutines.flow.Flow

/**
 * Base UseCase for single-shot operations with parameters.
 * 
 * Wrap multiple parameters in a data class:
 * ```
 * data class LoginParams(val email: String, val password: String)
 * class LoginUseCase : UseCase<LoginParams, User>() {
 *     override suspend fun invoke(params: LoginParams): Result<User> { ... }
 * }
 * ```
 * 
 * @param P Parameters type (use data class for multiple params)
 * @param R Return type
 */
abstract class UseCase<in P, out R> {
    abstract suspend operator fun invoke(params: P): Result<R>
}

/**
 * Base UseCase for single-shot operations without parameters.
 * 
 * ```
 * class GetProfileUseCase : NoParamUseCase<User>() {
 *     override suspend fun invoke(): Result<User> { ... }
 * }
 * ```
 * 
 * @param R Return type
 */
abstract class NoParamUseCase<out R> {
    abstract suspend operator fun invoke(): Result<R>
}

/**
 * UseCase that returns a Flow.
 * @param P Parameters type
 */
interface FlowUseCase<in P, out R> {
    operator fun invoke(params: P): Flow<Result<R>>
}

/**
 * UseCase that returns a Flow with no parameters.
 */
interface NoParamsFlowUseCase<out R> {
    operator fun invoke(): Flow<Result<R>>
}
