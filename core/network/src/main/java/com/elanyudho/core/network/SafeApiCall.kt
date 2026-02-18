package com.elanyudho.core.network

import com.elanyudho.core.base.wrapper.AppError
import com.elanyudho.core.base.wrapper.Result
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.plugins.*

import kotlinx.serialization.SerializationException
import java.io.IOException

/**
 * Central error handling wrapper for all Ktor API calls.
 * Catches specific exceptions and maps them to domain AppError.
 * 
 * Usage:
 * ```
 * suspend fun fetchUsers(): Result<List<User>> = safeApiCall {
 *     httpClient.get("users").body<List<User>>()
 * }
 * ```
 */
suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (e: ClientRequestException) {
        // 4xx Client Errors
        handleClientError(e)
    } catch (e: ServerResponseException) {
        // 5xx Server Errors
        handleServerError(e)
    } catch (e: SerializationException) {
        // JSON Parsing Errors
        handleSerializationError(e)
    } catch (e: HttpRequestTimeoutException) {
        // Timeout Errors
        handleTimeoutError(e)
    } catch (e: IOException) {
        // Network Errors (Android-specific)
        handleNetworkError(e)
    } catch (e: Exception) {
        // Generic exceptions
        handleGenericException(e)
    }
}

/**
 * Handles 4xx Client Request Errors.
 */
private suspend fun handleClientError(e: ClientRequestException): Result<Nothing> {
    val statusCode = e.response.status.value
    val errorBody = try {
        e.response.body<String>()
    } catch (_: Exception) {
        null
    }
    
    Napier.e("Client Error [$statusCode]: $errorBody", e, tag = "SafeApiCall")
    
    val error = when (statusCode) {
        400 -> AppError.BadRequest(
            message = errorBody ?: "Invalid request"
        )
        401 -> AppError.Unauthorized(
            message = errorBody ?: "Authentication required. Please login again."
        )
        403 -> AppError.Forbidden(
            message = errorBody ?: "Access denied. You don't have permission."
        )
        404 -> AppError.NotFound(
            message = errorBody ?: "Resource not found."
        )
        409 -> AppError.Conflict(
            message = errorBody ?: "Resource conflict."
        )
        422 -> AppError.ValidationError(
            message = errorBody ?: "Validation failed."
        )
        429 -> AppError.RateLimitError(
            message = errorBody ?: "Too many requests. Please try again later."
        )
        else -> AppError.BadRequest(
            message = errorBody ?: "Client error occurred",
            code = statusCode
        )
    }
    return Result.Error(error)
}

/**
 * Handles 5xx Server Response Errors.
 */
private suspend fun handleServerError(e: ServerResponseException): Result<Nothing> {
    val statusCode = e.response.status.value
    val errorBody = try {
        e.response.body<String>()
    } catch (_: Exception) {
        null
    }
    
    Napier.e("Server Error [$statusCode]: $errorBody", e, tag = "SafeApiCall")
    
    val error = when (statusCode) {
        503 -> AppError.ServiceUnavailable(
            message = errorBody ?: "Service temporarily unavailable."
        )
        else -> AppError.ServerError(
            message = errorBody ?: "Server error. Please try again later.",
            code = statusCode
        )
    }
    return Result.Error(error)
}

/**
 * Handles JSON Serialization Errors.
 */
private fun handleSerializationError(e: SerializationException): Result<Nothing> {
    Napier.e("Serialization Error: ${e.message}", e, tag = "SafeApiCall")
    return Result.Error(
        AppError.SerializationError(
            message = "Failed to parse server response."
        )
    )
}

/**
 * Handles Request Timeout Errors.
 */
private fun handleTimeoutError(e: HttpRequestTimeoutException): Result<Nothing> {
    Napier.e("Timeout Error: ${e.message}", e, tag = "SafeApiCall")
    return Result.Error(
        AppError.TimeoutError(
            message = "Request timed out. Please try again."
        )
    )
}

/**
 * Handles Network Errors (IOException).
 */
private fun handleNetworkError(e: IOException): Result<Nothing> {
    Napier.e("Network Error: ${e.message}", e, tag = "SafeApiCall")
    return Result.Error(
        AppError.NetworkError(
            message = "No internet connection. Please check your network."
        )
    )
}

/**
 * Handles generic exceptions.
 */
private fun handleGenericException(e: Exception): Result<Nothing> {
    Napier.e("Generic Error: ${e.message}", e, tag = "SafeApiCall")
    return Result.Error(
        AppError.UnknownError(
            message = e.message ?: "An unexpected error occurred.",
            throwable = e
        )
    )
}



/**
 * Retry wrapper for API calls.
 */
suspend fun <T> safeApiCallWithRetry(
    maxRetries: Int = 3,
    delayMs: Long = 1000L,
    shouldRetry: (AppError) -> Boolean = { it.isRetryable() },
    apiCall: suspend () -> T
): Result<T> {
    var currentAttempt = 0
    var lastError: AppError? = null
    
    while (currentAttempt < maxRetries) {
        when (val result = safeApiCall(apiCall)) {
            is Result.Success -> return result
            is Result.Error -> {
                lastError = result.error
                if (!shouldRetry(result.error)) {
                    return result
                }
                currentAttempt++
                if (currentAttempt < maxRetries) {
                    kotlinx.coroutines.delay(delayMs * currentAttempt)
                }
            }
            is Result.Loading -> { /* Should not happen */ }
        }
    }
    
    return Result.Error(lastError ?: AppError.UnknownError("Max retries exceeded"))
}
