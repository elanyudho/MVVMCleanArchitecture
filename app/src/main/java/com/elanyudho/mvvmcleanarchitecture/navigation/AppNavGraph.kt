package com.elanyudho.mvvmcleanarchitecture.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elanyudho.feature.auth.presentation.screen.LoginScreen

/**
 * App-level navigation graph using type-safe routes (Navigation 2.8+).
 * 
 * Routes are @Serializable objects/data classes defined in AppRoutes.
 * No more string-based route matching — fully type-safe at compile time.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login,
        modifier = modifier
    ) {
        // ==================== Auth ====================
        composable<AppRoutes.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoutes.Home) {
                        popUpTo<AppRoutes.Login> { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    // TODO: navController.navigate(AppRoutes.Register)
                },
                onNavigateToForgotPassword = {
                    // TODO: navController.navigate(AppRoutes.ForgotPassword)
                }
            )
        }
        
        // ==================== Home ====================
        composable<AppRoutes.Home> {
            // TODO: Replace with HomeScreen from feature:home module
            HomePlaceholder()
        }
        
        // === Add more destinations as features grow ===
        
        // composable<AppRoutes.Detail> { backStackEntry ->
        //     val detail: AppRoutes.Detail = backStackEntry.toRoute()
        //     DetailScreen(id = detail.id)
        // }
    }
}

/**
 * Temporary placeholder until feature:home is created.
 */
@Composable
private fun HomePlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
