package com.elanyudho.core.base

import com.elanyudho.core.base.wrapper.AppError

/**
 * Base interface for all UI states.
 * Enforces consistent loading and error handling across all screens.
 */
interface UiState {
    /**
     * Whether the UI is currently in a loading state.
     */
    val isLoading: Boolean
    
    /**
     * Current error, if any.
     */
    val error: AppError?
}

/**
 * Base interface for one-time UI events.
 * Use for navigation, toasts, snackbars, etc.
 */
interface UiEvent

/**
 * Common UI events that can be reused across features.
 */
sealed interface CommonEvent : UiEvent {
    data class ShowSnackbar(val message: String) : CommonEvent
    data class ShowToast(val message: String) : CommonEvent
    data object NavigateBack : CommonEvent
    data class NavigateToRoute(val route: String) : CommonEvent
}

/**
 * Extension to check if state has an active error.
 */
fun UiState.hasError(): Boolean = error != null

/**
 * Extension to check if state is idle (not loading and no error).
 */
fun UiState.isIdle(): Boolean = !isLoading && error == null
