package com.elanyudho.feature.auth.data.local

import com.elanyudho.core.database.dao.UserDao
import com.elanyudho.core.database.entity.UserEntity
import com.elanyudho.core.security.SecurePreferencesManager
import com.elanyudho.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local data source for auth feature.
 * 
 * Combines two storage mechanisms:
 * - Room (UserDao) → cached user data (name, email, avatar)
 * - SecurePreferencesManager → encrypted auth tokens (access, refresh)
 * 
 * This class is the single point of contact for all local data operations
 * in the auth feature.
 */
class AuthLocalDataSource(
    private val userDao: UserDao,
    private val securePrefs: SecurePreferencesManager
) {

    // ==================== User Data (Room) ====================

    /**
     * Observe the current user from Room DB.
     * FLOW: Emits whenever user data changes (insert, update, delete).
     */
    fun observeCurrentUser(): Flow<User?> {
        return securePrefs.userId.map { userId ->
            userId
        }.let { userIdFlow ->
            // We need to resolve the user from the DB
            // Simplified: observe based on stored userId
            userDao.observeAllUsers().map { users ->
                users.firstOrNull()?.toDomain()
            }
        }
    }

    /**
     * Get cached user once (not reactive).
     */
    suspend fun getCachedUser(userId: String): User? {
        return userDao.getUserById(userId)?.toDomain()
    }

    /**
     * Cache a user in Room DB.
     */
    suspend fun cacheUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    /**
     * Clear all cached user data.
     */
    suspend fun clearCachedUsers() {
        userDao.clearAll()
    }

    // ==================== Auth Tokens (Encrypted DataStore) ====================

    /**
     * Save complete session: tokens (encrypted) + user (Room cache).
     */
    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        user: User
    ) {
        // Save tokens to encrypted DataStore
        securePrefs.saveSession(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = user.id,
            userName = user.name,
            userEmail = user.email
        )
        // Cache user in Room
        userDao.insertUser(user.toEntity())
    }

    /**
     * Update tokens after refresh.
     */
    suspend fun updateTokens(accessToken: String, refreshToken: String? = null) {
        securePrefs.updateTokens(accessToken, refreshToken)
    }

    /**
     * Clear all local session data.
     */
    suspend fun clearSession() {
        securePrefs.clearSession()
        userDao.clearAll()
    }

    /** Observe login state */
    val isLoggedIn: Flow<Boolean> = securePrefs.isLoggedIn

    /** Observe access token */
    val accessToken: Flow<String> = securePrefs.accessToken

    // ==================== Mapping ====================

    private fun UserEntity.toDomain(): User = User(
        id = id,
        name = name,
        email = email
    )

    private fun User.toEntity(): UserEntity = UserEntity(
        id = id,
        name = name,
        email = email
    )
}
