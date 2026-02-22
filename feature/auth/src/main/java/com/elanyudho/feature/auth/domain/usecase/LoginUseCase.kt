package com.elanyudho.feature.auth.domain.usecase

import com.elanyudho.feature.auth.domain.model.User
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.core.base.data.wrapper.AppError
import com.elanyudho.core.base.presentation.usecase.UseCase
import com.elanyudho.feature.auth.domain.repository.AuthRepository

/**
 * Parameters for login operation.
 */
data class LoginParams(
    val email: String,
    val password: String
)

/**
 * UseCase for user login — demonstrates single-param UseCase with data class.
 * All validation lives here in the domain layer.
 */
class LoginUseCase(
    private val authRepository: AuthRepository
) : UseCase<LoginParams, User>() {
    
    override suspend fun invoke(params: LoginParams): Result<User> {
        // Input validation (domain layer responsibility)
        if (params.email.isBlank()) {
            return Result.error(
                AppError.ValidationError(
                    message = "Email is required",
                    errors = mapOf("email" to listOf("Email cannot be empty"))
                )
            )
        }
        
        if (!isValidEmail(params.email)) {
            return Result.error(
                AppError.ValidationError(
                    message = "Invalid email format",
                    errors = mapOf("email" to listOf("Please enter a valid email"))
                )
            )
        }
        
        if (params.password.isBlank()) {
            return Result.error(
                AppError.ValidationError(
                    message = "Password is required",
                    errors = mapOf("password" to listOf("Password cannot be empty"))
                )
            )
        }
        
        if (params.password.length < 6) {
            return Result.error(
                AppError.ValidationError(
                    message = "Password too short",
                    errors = mapOf("password" to listOf("Password must be at least 6 characters"))
                )
            )
        }
        
        return authRepository.login(params.email, params.password)
    }
    
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
