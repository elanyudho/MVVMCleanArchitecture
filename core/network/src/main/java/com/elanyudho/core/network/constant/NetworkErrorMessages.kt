package com.elanyudho.core.network.constant

/**
 * Centralized error message constants used by SafeApiCall.
 */
object NetworkErrorMessages {

    // Client Errors (4xx)
    const val BAD_REQUEST = "Invalid request"
    const val UNAUTHORIZED = "Authentication required. Please login again."
    const val FORBIDDEN = "Access denied. You don't have permission."
    const val NOT_FOUND = "Resource not found."
    const val CONFLICT = "Resource conflict."
    const val VALIDATION_FAILED = "Validation failed."
    const val RATE_LIMIT = "Too many requests. Please try again later."
    const val CLIENT_ERROR = "Client error occurred"

    // Server Errors (5xx)
    const val SERVICE_UNAVAILABLE = "Service temporarily unavailable."
    const val SERVER_ERROR = "Server error. Please try again later."

    // Other Errors
    const val SERIALIZATION_ERROR = "Failed to parse server response."
    const val TIMEOUT_ERROR = "Request timed out. Please try again."
    const val NETWORK_ERROR = "No internet connection. Please check your network."
    const val UNKNOWN_ERROR = "An unexpected error occurred."
    const val MAX_RETRIES_EXCEEDED = "Max retries exceeded"
}
