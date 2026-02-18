package com.elanyudho.core.security.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.elanyudho.core.security.AuthPreferences
import com.elanyudho.core.security.AuthPreferencesSerializer
import com.elanyudho.core.security.CryptoManager
import com.elanyudho.core.security.SecurePreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for the security layer.
 * Provides CryptoManager, encrypted DataStore, and SecurePreferencesManager.
 */
val securityModule = module {

    // CryptoManager (Tink encryption)
    single { CryptoManager(androidContext()) }

    // Encrypted Proto DataStore
    single<DataStore<AuthPreferences>> {
        DataStoreFactory.create(
            serializer = AuthPreferencesSerializer(cryptoManager = get()),
            produceFile = { androidContext().dataStoreFile("auth_prefs.pb") }
        )
    }

    // Public API for secure preferences
    single { SecurePreferencesManager(dataStore = get()) }
}
