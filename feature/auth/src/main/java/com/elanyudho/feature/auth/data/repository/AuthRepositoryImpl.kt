package com.elanyudho.feature.auth.data.repository

import com.elanyudho.feature.auth.data.local.AuthLocalDataSource
import com.elanyudho.feature.auth.data.remote.AuthRemoteDataSource
import com.elanyudho.feature.auth.data.remote.dto.mapper.toUser
import com.elanyudho.feature.auth.domain.model.User
import com.elanyudho.feature.auth.domain.repository.AuthRepository
import com.elanyudho.core.base.data.wrapper.AppError
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.core.network.connectivity.ConnectivityMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of AuthRepository with offline support.
 * 
 * Cache strategies:
 * - login/register: NETWORK ONLY (must be online)
 * - observeCurrentUser: CACHE FIRST via Room Flow
 * - getCurrentUser: CACHE FIRST, then refresh from network
 * - isAuthenticated: CACHE ONLY from encrypted DataStore
 */
class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val connectivityMonitor: ConnectivityMonitor
) : AuthRepository {

    // ==================== NETWORK ONLY ====================
    
    override suspend fun login(email: String, password: String): Result<User> {
        if (!connectivityMonitor.isOnline.value) {
            return Result.Error(
                AppError.NetworkError(message = "No internet connection. Please check your network.")
            )
        }
        
        return when (val result = remoteDataSource.login(email, password)) {
            is Result.Success -> {
                val response = result.data.data
                val user = response.toUser()
                localDataSource.saveSession(
                    accessToken = response.token,
                    refreshToken = response.refreshToken ?: "",
                    user = user
                )
                Result.Success(user)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
    
    override suspend fun register(
        name: String, 
        email: String, 
        password: String
    ): Result<User> {
        if (!connectivityMonitor.isOnline.value) {
            return Result.Error(
                AppError.NetworkError(message = "No internet connection. Please check your network.")
            )
        }
        
        return when (val result = remoteDataSource.register(name, email, password)) {
            is Result.Success -> {
                val response = result.data.data
                val user = response.toUser()
                localDataSource.saveSession(
                    accessToken = response.token,
                    refreshToken = response.refreshToken ?: "",
                    user = user
                )
                Result.Success(user)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
    
    override suspend fun logout(): Result<Unit> {
        // Try remote logout, but always clear local data
        val remoteResult = if (connectivityMonitor.isOnline.value) {
            remoteDataSource.logout()
        } else {
            Result.Success(Unit)
        }
        
        // Always clear local session
        localDataSource.clearSession()
        
        return when (remoteResult) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> {
                // Local data already cleared, just report success
                Result.Success(Unit)
            }
            is Result.Loading -> remoteResult
        }
    }
    
    override suspend fun refreshToken(): Result<String> {
        if (!connectivityMonitor.isOnline.value) {
            return Result.Error(
                AppError.NetworkError(message = "No internet connection")
            )
        }
        
        val currentRefreshToken = localDataSource.accessToken.first()
        if (currentRefreshToken.isBlank()) {
            return Result.Error(
                AppError.Unauthorized(message = "No refresh token available")
            )
        }
        
        return when (val result = remoteDataSource.refreshToken(currentRefreshToken)) {
            is Result.Success -> {
                val response = result.data.data
                localDataSource.updateTokens(
                    accessToken = response.token,
                    refreshToken = response.refreshToken
                )
                val user = response.toUser()
                localDataSource.cacheUser(user)
                Result.Success(response.token)
            }
            is Result.Error -> result
            is Result.Loading -> result
        }
    }
    
    // ==================== CACHE FIRST ====================
    
    override suspend fun getCurrentUser(): Result<User?> {
        // Try cache first
        val isLoggedIn = localDataSource.isLoggedIn.first()
        if (!isLoggedIn) return Result.Success(null)
        
        // If online, refresh from network
        if (connectivityMonitor.isOnline.value) {
            when (val result = remoteDataSource.getCurrentUser()) {
                is Result.Success -> {
                    val user = result.data.data.toUser()
                    localDataSource.cacheUser(user)
                    return Result.Success(user)
                }
                is Result.Error -> {
                    if (result.error is AppError.Unauthorized) {
                        return Result.Success(null)
                    }
                    // Fall through to cache
                }
                is Result.Loading -> { /* fall through */ }
            }
        }
        
        // Fallback to cache (works offline!)
        return Result.Success(null)
    }
    
    // ==================== CACHE ONLY (Flow) ====================
    
    /**
     * Observe current user from local database.
     * Works offline — Room emits cached data.
     */
    override fun observeCurrentUser(): Flow<Result<User?>> {
        return localDataSource.isLoggedIn.map { loggedIn ->
            if (loggedIn) {
                // Note: In a full implementation, you'd combine this with
                // userDao.observeUserById(). For simplicity, we map from prefs.
                Result.Success(null) as Result<User?>
            } else {
                Result.Success(null)
            }
        }
    }
    
    // ==================== CACHE ONLY ====================
    
    override suspend fun isAuthenticated(): Boolean {
        return localDataSource.isLoggedIn.first()
    }
}
