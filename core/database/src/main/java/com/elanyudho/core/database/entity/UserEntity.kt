package com.elanyudho.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a cached User.
 * 
 * This is the local database representation.
 * Use EntityMappers to convert between this and domain models.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val lastUpdatedAt: Long = System.currentTimeMillis()
)
