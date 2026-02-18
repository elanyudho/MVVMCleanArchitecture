package com.elanyudho.core.security

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Clean API for reading/writing secure auth preferences.
 * 
 * This is what the rest of the app interacts with.
 * All data is encrypted at rest via Tink + Proto DataStore.
 * 
 * Usage:
 *   val isLoggedIn: Flow<Boolean> = securePrefs.isLoggedIn
 *   securePrefs.saveSession(token, refreshToken, userId, userName, userEmail)
 *   securePrefs.clearSession()
 */
class SecurePreferencesManager(
    private val dataStore: DataStore<AuthPreferences>
) {

    // ==================== Read (Flow — reactive) ====================

    /** Observe login state reactively */
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it.isLoggedIn }

    /** Observe access token */
    val accessToken: Flow<String> = dataStore.data.map { it.accessToken }

    /** Observe refresh token */
    val refreshToken: Flow<String> = dataStore.data.map { it.refreshToken }

    /** Observe user ID */
    val userId: Flow<String> = dataStore.data.map { it.userId }

    /** Observe user name */
    val userName: Flow<String> = dataStore.data.map { it.userName }

    /** Observe user email */
    val userEmail: Flow<String> = dataStore.data.map { it.userEmail }

    /** Observe all preferences as a single object */
    val authPreferences: Flow<AuthPreferences> = dataStore.data

    // ==================== Write (Suspend — one-shot) ====================

    /**
     * Save complete auth session after login/register.
     */
    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        userId: String,
        userName: String,
        userEmail: String
    ) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setUserId(userId)
                .setUserName(userName)
                .setUserEmail(userEmail)
                .setIsLoggedIn(true)
                .setTokenExpiryMs(System.currentTimeMillis() + TOKEN_EXPIRY_DURATION_MS)
                .build()
        }
    }

    /**
     * Update tokens after a token refresh.
     */
    suspend fun updateTokens(accessToken: String, refreshToken: String? = null) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setAccessToken(accessToken)
                .apply { refreshToken?.let { setRefreshToken(it) } }
                .setTokenExpiryMs(System.currentTimeMillis() + TOKEN_EXPIRY_DURATION_MS)
                .build()
        }
    }

    /**
     * Clear all session data on logout.
     */
    suspend fun clearSession() {
        dataStore.updateData {
            AuthPreferences.getDefaultInstance()
        }
    }

    companion object {
        /** Token valid for 1 hour by default */
        private const val TOKEN_EXPIRY_DURATION_MS = 60 * 60 * 1000L
    }
}
