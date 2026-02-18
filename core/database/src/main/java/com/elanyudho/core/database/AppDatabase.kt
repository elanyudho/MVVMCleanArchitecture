package com.elanyudho.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.elanyudho.core.database.dao.UserDao
import com.elanyudho.core.database.entity.UserEntity

/**
 * Room database for the application.
 * 
 * Add new entities and DAOs here as the app grows.
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
