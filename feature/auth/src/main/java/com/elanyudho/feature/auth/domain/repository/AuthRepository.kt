package com.elanyudho.feature.auth.domain.repository

import com.elanyudho.feature.auth.domain.model.User
import com.elanyudho.core.base.data.wrapper.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 * Implementations handle the actual data sources.
 */
interface AuthRepository {
    
    /**
     * Performs user login with email and password.
     * ONE-SHOT: Returns a single Result.
     */
    suspend fun login(email: String, password: String): Result<User>
    
    /**
     * Performs user registration.
     * ONE-SHOT: Returns a single Result.
     */
    suspend fun register(name: String, email: String, password: String): Result<User>
    
    /**
     * Logs out the current user.
     * ONE-SHOT: Returns a single Result.
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * Refreshes the authentication token.
     * ONE-SHOT: Returns a single Result.
     */
    suspend fun refreshToken(): Result<String>
    
    /**
     * Gets the currently logged in user once.
     * ONE-SHOT: Returns a single Result.
     */
    suspend fun getCurrentUser(): Result<User?>
    
    /**
     * Observes the current user session reactively.
     * FLOW: Emits new Result<User?> every time user state changes
     * (login, logout, token refresh, etc).
     * 
     * Use this when you want the UI to automatically update
     * whenever the user session changes.
     */
    fun observeCurrentUser(): Flow<Result<User?>>
    
    /**
     * Checks if user is currently authenticated.
     */
    suspend fun isAuthenticated(): Boolean
}
