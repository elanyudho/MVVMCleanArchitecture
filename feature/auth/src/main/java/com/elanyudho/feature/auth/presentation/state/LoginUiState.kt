package com.elanyudho.feature.auth.presentation.state

import com.elanyudho.feature.auth.domain.model.User
import com.elanyudho.core.base.data.wrapper.AppError
import com.elanyudho.core.base.presentation.state.UiEvent
import com.elanyudho.core.base.presentation.state.UiState

/**
 * UI State for the Login screen.
 */
data class LoginUiState(
    override val isLoading: Boolean = false,
    override val error: AppError? = null,
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val user: User? = null
) : UiState {
    
    /**
     * Check if login button should be enabled.
     * Only checks non-empty — full validation is in UseCase.
     */
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && 
                password.isNotBlank() && 
                !isLoading
                
    /**
     * Check if user is logged in.
     */
    val isLoggedIn: Boolean
        get() = user != null
}

/**
 * One-time events for the Login screen.
 */
sealed class LoginEvent : UiEvent {
    data object NavigateToHome : LoginEvent()
    data object NavigateToRegister : LoginEvent()
    data object NavigateToForgotPassword : LoginEvent()
    data class ShowToast(val message: String) : LoginEvent()
    data class ShowSnackbar(val message: String) : LoginEvent()
}
