package com.elanyudho.core.network.di

import com.elanyudho.core.network.KtorClient
import com.elanyudho.core.network.connectivity.ConnectivityMonitor
import com.elanyudho.core.security.SecurePreferencesManager
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Network module providing Ktor HttpClient and ConnectivityMonitor.
 */
val networkModule = module {
    
    // ConnectivityMonitor (singleton, BroadcastReceiver-based)
    single { ConnectivityMonitor(androidContext()) }
    
    /**
     * Provides configured HttpClient singleton.
     * Injects auth token from SecurePreferencesManager per-request.
     * 
     * TODO: Replace with your actual API base URL.
     */
    single<HttpClient> {
        val securePrefs: SecurePreferencesManager = get()
        
        KtorClient.create(
            baseUrl = "https://api.example.com/v1/",
            authToken = {
                // Read token from encrypted DataStore
                // runBlocking is safe here: called per HTTP request, reads from in-memory cache
                runBlocking { securePrefs.accessToken.first().ifBlank { null } }
            },
            enableLogging = true
        )
    }
}
