package com.elanyudho.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elanyudho.core.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User.
 * 
 * Note the difference between suspend and Flow:
 * - suspend functions → one-shot queries
 * - Flow functions → reactive queries (auto-update when data changes)
 */
@Dao
interface UserDao {

    /**
     * Observe a user by ID reactively.
     * FLOW: Emits new UserEntity whenever the row changes.
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    fun observeUserById(userId: String): Flow<UserEntity?>

    /**
     * Observe all users reactively.
     * FLOW: Emits new list whenever any user row changes.
     */
    @Query("SELECT * FROM users ORDER BY lastUpdatedAt DESC")
    fun observeAllUsers(): Flow<List<UserEntity>>

    /**
     * Get a user by ID once.
     * ONE-SHOT: Returns the current value, does not observe.
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    /**
     * Insert or replace a user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    /**
     * Delete a specific user.
     */
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)

    /**
     * Delete all cached users.
     */
    @Query("DELETE FROM users")
    suspend fun clearAll()
}
