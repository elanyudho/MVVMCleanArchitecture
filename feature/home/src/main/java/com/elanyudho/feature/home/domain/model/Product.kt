package com.elanyudho.feature.home.domain.model

/**
 * Domain model representing a Product.
 * Used for horizontal paginated lists in ExploreScreen.
 */
data class Product(
    val id: Int,
    val name: String,
    val price: String,
    val category: String
)
