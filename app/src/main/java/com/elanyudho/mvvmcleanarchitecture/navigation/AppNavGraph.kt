package com.elanyudho.mvvmcleanarchitecture.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elanyudho.feature.auth.presentation.screen.LoginScreen
import com.elanyudho.feature.home.presentation.screen.ExploreScreen
import com.elanyudho.feature.home.presentation.screen.HomeScreen

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
            HomeScreen(
                onNavigateToExplore = {
                    navController.navigate(AppRoutes.Explore)
                }
            )
        }
        
        // ==================== Explore ====================
        composable<AppRoutes.Explore> {
            ExploreScreen()
        }
    }
}
