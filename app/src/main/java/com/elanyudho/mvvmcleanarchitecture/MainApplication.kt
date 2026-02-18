package com.elanyudho.mvvmcleanarchitecture

import android.app.Application
import com.elanyudho.core.database.di.databaseModule
import com.elanyudho.core.network.di.networkModule
import com.elanyudho.core.security.di.securityModule
import com.elanyudho.feature.auth.di.authModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class for Android.
 * Initializes Koin DI and Napier logging.
 */
class MainApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Napier for logging
        Napier.base(DebugAntilog())
        
        // Initialize Koin with all modules
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                // Core modules
                securityModule,    // Tink + DataStore (encrypted token storage)
                databaseModule,    // Room (data cache)
                networkModule,     // Ktor + ConnectivityMonitor
                
                // Feature modules
                authModule
            )
        }
    }
}
