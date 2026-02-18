package com.elanyudho.mvvmcleanarchitecture.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using @Serializable.
 * 
 * Navigation 2.8+ supports data class/object routes:
 * - object → screen tanpa parameter
 * - data class → screen dengan parameter
 * 
 * Usage:
 *   navController.navigate(AppRoutes.Login)
 *   navController.navigate(AppRoutes.Detail(id = "123"))
 */
object AppRoutes {
    
    @Serializable
    object Login
    
    @Serializable
    object Home
    
    // === Add more routes as features grow ===
    
    // @Serializable
    // object Register
    
    // @Serializable
    // object ForgotPassword
    
    // @Serializable
    // data class Detail(val id: String)
    
    // @Serializable
    // data class Profile(val userId: String)
}
