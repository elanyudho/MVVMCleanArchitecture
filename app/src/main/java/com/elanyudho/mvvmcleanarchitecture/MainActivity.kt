package com.elanyudho.mvvmcleanarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.elanyudho.core.network.connectivity.ConnectivityMonitor
import com.elanyudho.core.ui.component.OfflineBanner
import com.elanyudho.core.ui.theme.MVVMCleanArchitectureTheme
import com.elanyudho.mvvmcleanarchitecture.navigation.AppNavGraph
import org.koin.android.ext.android.inject

/**
 * Main Activity — hosts Scaffold with OfflineBanner + NavHost.
 * 
 * OfflineBanner is placed ONCE here and is visible on ALL screens.
 * ConnectivityMonitor (with BroadcastReceiver) is injected via Koin.
 */
class MainActivity : ComponentActivity() {

    private val connectivityMonitor: ConnectivityMonitor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MVVMCleanArchitectureTheme {
                val isOnline by connectivityMonitor.isOnline.collectAsState()
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // OfflineBanner — visible on ALL screens when offline
                        OfflineBanner(isOffline = !isOnline)
                        
                        // Navigation host — all screens render here
                        AppNavGraph(
                            navController = navController,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}