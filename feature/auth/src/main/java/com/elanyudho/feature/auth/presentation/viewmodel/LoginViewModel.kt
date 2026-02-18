package com.elanyudho.feature.auth.presentation.viewmodel

import com.elanyudho.core.base.BaseViewModel
import com.elanyudho.feature.auth.domain.usecase.LoginParams
import com.elanyudho.feature.auth.domain.usecase.LoginUseCase
import com.elanyudho.feature.auth.domain.usecase.ObserveCurrentUserUseCase
import com.elanyudho.feature.auth.presentation.state.LoginEvent
import com.elanyudho.feature.auth.presentation.state.LoginUiState

/**
 * ViewModel for the Login screen.
 * 
 * Demonstrates both patterns:
 * - ONE-SHOT (LoginUseCase) → for login action
 * - FLOW (ObserveCurrentUserUseCase) → for observing user session
 * 
 * Validation is handled by UseCase (domain layer).
 * ViewModel only checks empty fields for enabling/disabling the login button.
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase
) : BaseViewModel<LoginUiState, LoginEvent>(LoginUiState()) {

    init {
        observeUserSession()
    }

    /**
     * FLOW pattern: Observe user session reactively.
     */
    private fun observeUserSession() {
        launchWithFlow(
            setLoading = { copy(isLoading = it) },
            setError = { copy(error = it) },
            onSuccess = { user ->
                updateState { copy(user = user) }
                if (user != null) {
                    sendEvent(LoginEvent.NavigateToHome)
                }
            },
            flow = observeCurrentUserUseCase()
        )
    }

    // ==================== User Input Handlers ====================

    fun onEmailChanged(email: String) {
        updateState { copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        updateState { copy(password = password) }
    }

    fun onTogglePasswordVisibility() {
        updateState { copy(isPasswordVisible = !isPasswordVisible) }
    }

    // ==================== Actions ====================

    /**
     * ONE-SHOT pattern: Perform login action.
     * Validation is done by LoginUseCase in domain layer.
     */
    fun onLoginClicked() {
        launchWithLoading(
            setLoading = { copy(isLoading = it) },
            setError = { copy(error = it) },
            onSuccess = { user ->
                sendEvent(LoginEvent.ShowToast("Welcome, ${user.name}!"))
            }
        ) {
            loginUseCase(LoginParams(currentState.email, currentState.password))
        }
    }

    fun onRegisterClicked() {
        sendEvent(LoginEvent.NavigateToRegister)
    }

    fun onForgotPasswordClicked() {
        sendEvent(LoginEvent.NavigateToForgotPassword)
    }

    fun onDismissError() {
        clearError { copy(error = it) }
    }

    fun onRetry() {
        onDismissError()
        onLoginClicked()
    }
}