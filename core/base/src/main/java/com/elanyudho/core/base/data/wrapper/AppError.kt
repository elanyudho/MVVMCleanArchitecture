package com.elanyudho.core.base.data.wrapper

/**
 * Sealed class representing application-level errors.
 * Maps HTTP codes and exceptions to domain-specific errors.
 */
sealed class AppError(
    open val message: String,
    open val code: Int? = null
) {
    
    // ==================== Network Errors ====================
    
    /**
     * No internet connection or network unavailable.
     */
    data class NetworkError(
        override val message: String = "No internet connection. Please check your network."
    ) : AppError(message)
    
    /**
     * Request timeout.
     */
    data class TimeoutError(
        override val message: String = "Request timed out. Please try again."
    ) : AppError(message)
    
    // ==================== Client Errors (4xx) ====================
    
    /**
     * 400 Bad Request - Invalid request format or parameters.
     */
    data class BadRequest(
        override val message: String = "Invalid request",
        override val code: Int = 400
    ) : AppError(message, code)
    
    /**
     * 401 Unauthorized - Authentication required or failed.
     */
    data class Unauthorized(
        override val message: String = "Authentication required. Please login again."
    ) : AppError(message, 401)
    
    /**
     * 403 Forbidden - Access denied.
     */
    data class Forbidden(
        override val message: String = "Access denied. You don't have permission."
    ) : AppError(message, 403)
    
    /**
     * 404 Not Found - Resource doesn't exist.
     */
    data class NotFound(
        override val message: String = "Resource not found."
    ) : AppError(message, 404)
    
    /**
     * 409 Conflict - Resource already exists or conflict.
     */
    data class Conflict(
        override val message: String = "Resource conflict."
    ) : AppError(message, 409)
    
    /**
     * 422 Unprocessable Entity - Validation error.
     */
    data class  ValidationError(
        override val message: String = "Validation failed.",
        val errors: Map<String, List<String>> = emptyMap()
    ) : AppError(message, 422)
    
    /**
     * 429 Too Many Requests - Rate limit exceeded.
     */
    data class RateLimitError(
        override val message: String = "Too many requests. Please try again later."
    ) : AppError(message, 429)
    
    // ==================== Server Errors (5xx) ====================
    
    /**
     * 500-599 Server Error - Server-side issues.
     */
    data class ServerError(
        override val message: String = "Server error. Please try again later.",
        override val code: Int = 500
    ) : AppError(message, code)
    
    /**
     * 503 Service Unavailable - Server is down or under maintenance.
     */
    data class ServiceUnavailable(
        override val message: String = "Service temporarily unavailable. Please try again later."
    ) : AppError(message, 503)
    
    // ==================== Parsing Errors ====================
    
    /**
     * Failed to parse/serialize response.
     */
    data class SerializationError(
        override val message: String = "Failed to parse response."
    ) : AppError(message)
    
    // ==================== Generic Errors ====================
    
    /**
     * Unknown/unexpected error.
     */
    data class UnknownError(
        override val message: String = "An unexpected error occurred.",
        val throwable: Throwable? = null
    ) : AppError(message)
    
    /**
     * Custom error for business logic.
     */
    data class BusinessError(
        override val message: String,
        val errorCode: String? = null
    ) : AppError(message)
    
    companion object {
        /**
         * Factory method to create AppError from HTTP status code.
         */
        fun fromHttpCode(code: Int, message: String? = null): AppError = when (code) {
            400 -> BadRequest(message ?: "Invalid request")
            401 -> Unauthorized(message ?: "Authentication required")
            403 -> Forbidden(message ?: "Access denied")
            404 -> NotFound(message ?: "Resource not found")
            409 -> Conflict(message ?: "Resource conflict")
            422 -> ValidationError(message ?: "Validation failed")
            429 -> RateLimitError(message ?: "Too many requests")
            500 -> ServerError(message ?: "Internal server error", 500)
            502 -> ServerError(message ?: "Bad gateway", 502)
            503 -> ServiceUnavailable(message ?: "Service unavailable")
            504 -> ServerError(message ?: "Gateway timeout", 504)
            in 400..499 -> BadRequest(message ?: "Client error", code)
            in 500..599 -> ServerError(message ?: "Server error", code)
            else -> UnknownError(message ?: "Unknown error")
        }
    }
    
    /**
     * Returns a user-friendly display message.
     */
    fun displayMessage(): String = message
    
    /**
     * Checks if error is authentication related.
     */
    fun isAuthError(): Boolean = this is Unauthorized || this is Forbidden
    
    /**
     * Checks if error should trigger retry.
     */
    fun isRetryable(): Boolean = when (this) {
        is NetworkError, is TimeoutError, is ServerError, is ServiceUnavailable -> true
        else -> false
    }
}
