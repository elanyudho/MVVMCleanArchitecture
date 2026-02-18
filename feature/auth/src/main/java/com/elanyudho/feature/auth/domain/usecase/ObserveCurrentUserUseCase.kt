package com.elanyudho.feature.auth.domain.usecase

import com.elanyudho.feature.auth.domain.model.User
import com.elanyudho.feature.auth.domain.repository.AuthRepository
import com.elanyudho.core.base.wrapper.Result
import kotlinx.coroutines.flow.Flow

/**
 * Observe the current user session reactively.
 * 
 * Flow use cases don't need a base class — just return Flow<Result<T>> directly.
 * 
 * Usage in ViewModel:
 * ```
 * launchWithFlow(
 *     ...,
 *     flow = observeCurrentUserUseCase()  // Emits continuously
 * )
 * ```
 */
class ObserveCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Returns a Flow that emits Result<User?> whenever the user session changes.
     */
    operator fun invoke(): Flow<Result<User?>> {
        return authRepository.observeCurrentUser()
    }
}
