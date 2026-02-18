package com.elanyudho.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elanyudho.core.base.wrapper.AppError
import com.elanyudho.core.base.wrapper.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel for all features.
 * 
 * @param State The UI state type, must implement UiState
 * @param Event The UI event type, must implement UiEvent
 * @param initialState The initial state of the screen
 * @param dispatcher The coroutine dispatcher for background work
 */
abstract class BaseViewModel<State : UiState, Event : UiEvent>(
    initialState: State,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    
    // ==================== State Management ====================
    
    private val _uiState = MutableStateFlow(initialState)
    
    /**
     * Observable UI state.
     */
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    
    /**
     * Current state value.
     */
    protected val currentState: State get() = _uiState.value
    
    // ==================== Event Management ====================
    
    private val _events = MutableSharedFlow<Event>(
        extraBufferCapacity = 64
    )
    
    /**
     * One-time events stream for navigation, toasts, etc.
     */
    val events = _events.asSharedFlow()
    
    // ==================== State Updates ====================
    
    /**
     * Update the UI state atomically.
     * @param reducer Function that produces the new state from current state
     */
    protected fun updateState(reducer: State.() -> State) {
        _uiState.update { it.reducer() }
    }
    
    /**
     * Set a completely new state.
     */
    protected fun setState(newState: State) {
        _uiState.value = newState
    }
    
    // ==================== Event Emission ====================
    
    /**
     * Emit a one-time event.
     */
    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
    
    // ==================== Coroutine Helpers ====================
    
    /**
     * Launch a coroutine with automatic loading state management.
     * Automatically sets isLoading = true/false and handles errors.
     * 
     * @param setLoading Lambda to create new state with loading flag
     * @param setError Lambda to create new state with error
     * @param onSuccess Callback when Result is Success
     * @param block Suspend function that returns Result<T>
     */
    protected fun <T> launchWithLoading(
        setLoading: State.(Boolean) -> State,
        setError: State.(AppError?) -> State,
        onSuccess: (T) -> Unit = {},
        block: suspend () -> Result<T>
    ) {
        viewModelScope.launch(dispatcher) {
            // Set loading to true, clear previous error
            updateState { 
                setLoading(true).setError(null)
            }
            
            // Execute the API call
            when (val result = block()) {
                is Result.Success -> {
                    updateState { 
                        setLoading(false)
                    }
                    onSuccess(result.data)
                }
                is Result.Error -> {
                    updateState { 
                        setLoading(false).setError(result.error)
                    }
                }
                is Result.Loading -> {
                    // Already handled above
                }
            }
        }
    }
    
    /**
     * Launch a coroutine with loading state, collecting from a Flow.
     */
    protected fun <T> launchWithFlow(
        setLoading: State.(Boolean) -> State,
        setError: State.(AppError?) -> State,
        onSuccess: (T) -> Unit = {},
        flow: Flow<Result<T>>
    ) {
        viewModelScope.launch(dispatcher) {
            flow.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        updateState { setLoading(true).setError(null) }
                    }
                    is Result.Success -> {
                        updateState { setLoading(false) }
                        onSuccess(result.data)
                    }
                    is Result.Error -> {
                        updateState { setLoading(false).setError(result.error) }
                    }
                }
            }
        }
    }
    
    /**
     * Launch a coroutine without loading state management.
     */
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch(dispatcher) {
            block()
        }
    }
    
    /**
     * Launch a coroutine on the main thread.
     */
    protected fun launchMain(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            block()
        }
    }
    
    // ==================== Error Handling ====================
    
    /**
     * Clear current error state.
     */
    protected fun clearError(setError: State.(AppError?) -> State) {
        updateState { setError(null) }
    }
    
    /**
     * Handle error with optional custom action.
     */
    protected fun handleError(
        error: AppError,
        setError: State.(AppError?) -> State,
        onAuthError: () -> Unit = {}
    ) {
        updateState { setError(error) }
        
        // Handle authentication errors specially
        if (error.isAuthError()) {
            onAuthError()
        }
    }
}
