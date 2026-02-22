package com.elanyudho.core.network

import com.elanyudho.core.base.data.response.ErrorModel
import com.elanyudho.core.base.data.wrapper.AppError
import com.elanyudho.core.base.data.wrapper.Result
import com.elanyudho.core.network.constant.NetworkErrorMessages
import com.elanyudho.core.network.model.ErrorBodyResponse
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.delay

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * JSON instance for parsing error bodies.
 * Lenient + ignoreUnknownKeys to handle any server format.
 */
private val errorJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

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
        handleClientError(e)
    } catch (e: ServerResponseException) {
        handleServerError(e)
    } catch (e: SerializationException) {
        handleSerializationError(e)
    } catch (e: HttpRequestTimeoutException) {
        handleTimeoutError(e)
    } catch (e: IOException) {
        handleNetworkError(e)
    } catch (e: Exception) {
        handleGenericException(e)
    }
}

/**
 * Parses an error response body string.
 * Returns null if parsing fails (fallback to raw string).
 */
private fun parseErrorBody(body: String?): ErrorBodyResponse? {
    if (body.isNullOrBlank()) return null
    return try {
        errorJson.decodeFromString<ErrorBodyResponse>(body)
    } catch (_: Exception) {
        null
    }
}

/**
 * Formats error details from [ErrorModel] list into a readable string.
 *
 * Example output: "email: already taken, password: too short"
 */
private fun formatErrorDetails(errors: List<ErrorModel>?): String? {
    if (errors.isNullOrEmpty()) return null
    return errors.joinToString(", ") { error ->
        if (error.field != null) "${error.field}: ${error.message}" else ""
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
    
    val parsed = parseErrorBody(errorBody)
    val errorMessage = parsed?.message ?: errorBody
    val errorDetails = formatErrorDetails(parsed?.errors)
    
    val fullMessage = if (errorDetails != null && errorMessage != null) {
        "$errorMessage \n$errorDetails"
    } else {
        errorMessage
    }
    
    Napier.e("Client Error [$statusCode]: $fullMessage", e, tag = "SafeApiCall")
    
    val error = when (statusCode) {
        400 -> AppError.BadRequest(
            message = fullMessage ?: NetworkErrorMessages.BAD_REQUEST
        )
        401 -> AppError.Unauthorized(
            message = parsed?.message ?: NetworkErrorMessages.UNAUTHORIZED
        )
        403 -> AppError.Forbidden(
            message = parsed?.message ?: NetworkErrorMessages.FORBIDDEN
        )
        404 -> AppError.NotFound(
            message = parsed?.message ?: NetworkErrorMessages.NOT_FOUND
        )
        409 -> AppError.Conflict(
            message = parsed?.message ?: NetworkErrorMessages.CONFLICT
        )
        422 -> AppError.ValidationError(
            message = parsed?.message ?: NetworkErrorMessages.VALIDATION_FAILED,
            errors = parsed?.errors
                ?.groupBy { it.field ?: "_general" }
                ?.mapValues { (_, models) -> models.map { it.message } }
                ?: emptyMap()
        )
        429 -> AppError.RateLimitError(
            message = parsed?.message ?: NetworkErrorMessages.RATE_LIMIT
        )
        else -> AppError.BadRequest(
            message = fullMessage ?: NetworkErrorMessages.CLIENT_ERROR,
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
    
    val parsed = parseErrorBody(errorBody)
    val errorMessage = parsed?.message ?: errorBody
    
    Napier.e("Server Error [$statusCode]: $errorMessage", e, tag = "SafeApiCall")
    
    val error = when (statusCode) {
        503 -> AppError.ServiceUnavailable(
            message = parsed?.message ?: NetworkErrorMessages.SERVICE_UNAVAILABLE
        )
        else -> AppError.ServerError(
            message = parsed?.message ?: NetworkErrorMessages.SERVER_ERROR,
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
            message = NetworkErrorMessages.SERIALIZATION_ERROR
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
            message = NetworkErrorMessages.TIMEOUT_ERROR
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
            message = NetworkErrorMessages.NETWORK_ERROR
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
            message = e.message ?: NetworkErrorMessages.UNKNOWN_ERROR,
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
                    delay(delayMs * currentAttempt)
                }
            }
            is Result.Loading -> { /* Should not happen */ }
        }
    }
    
    return Result.Error(lastError ?: AppError.UnknownError(NetworkErrorMessages.MAX_RETRIES_EXCEEDED))
}
