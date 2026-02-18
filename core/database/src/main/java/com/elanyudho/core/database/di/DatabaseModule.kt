package com.elanyudho.core.database.di

import androidx.room.Room
import com.elanyudho.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for the database layer.
 * Provides Room database and DAOs.
 */
val databaseModule = module {

    // Room Database (singleton)
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "mvvm_clean_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    // DAOs
    single { get<AppDatabase>().userDao() }
}
