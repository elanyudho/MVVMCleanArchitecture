package com.elanyudho.core.base.data.wrapper

/**
 * Sealed class for wrapping API results.
 * Provides a type-safe way to handle success, error, and loading states.
 */
sealed class Result<out T> {
    
    /**
     * Represents a successful result with data.
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents an error result with AppError.
     */
    data class Error(val error: AppError) : Result<Nothing>()
    
    /**
     * Represents a loading state.
     */
    data object Loading : Result<Nothing>()
    
    // State checks
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading
    
    /**
     * Returns the data if Success, null otherwise.
     */
    fun getOrNull(): T? = (this as? Success)?.data
    
    /**
     * Returns the error if Error, null otherwise.
     */
    fun errorOrNull(): AppError? = (this as? Error)?.error
    
    /**
     * Returns the data if Success, or the default value otherwise.
     */
    fun getOrDefault(defaultValue: @UnsafeVariance T): T = 
        (this as? Success)?.data ?: defaultValue
    
    /**
     * Returns the data if Success, or throws the error message.
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw Exception(error.message)
        is Loading -> throw IllegalStateException("Result is still loading")
    }
    
    /**
     * Transforms the data if Success.
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Loading -> this
    }
    
    /**
     * Transforms the data if Success, allowing for another Result.
     */
    inline fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(data)
        is Error -> this
        is Loading -> this
    }
    
    /**
     * Executes action if Success.
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Executes action if Error.
     */
    inline fun onError(action: (AppError) -> Unit): Result<T> {
        if (this is Error) action(error)
        return this
    }
    
    /**
     * Executes action if Loading.
     */
    inline fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) action()
        return this
    }
    
    /**
     * Folds the result into a single value.
     */
    inline fun <R> fold(
        onSuccess: (T) -> R,
        onError: (AppError) -> R,
        onLoading: () -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError(error)
        is Loading -> onLoading()
    }
    
    companion object {
        /**
         * Creates a Success result.
         */
        fun <T> success(data: T): Result<T> = Success(data)
        
        /**
         * Creates an Error result.
         */
        fun error(error: AppError): Result<Nothing> = Error(error)
        
        /**
         * Creates a Loading result.
         */
        fun loading(): Result<Nothing> = Loading
    }
}
