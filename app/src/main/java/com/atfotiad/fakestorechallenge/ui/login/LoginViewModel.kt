package com.atfotiad.fakestorechallenge.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.atfotiad.fakestorechallenge.data.model.LoginRequest
import com.atfotiad.fakestorechallenge.security.TokenManager
import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCase
import com.atfotiad.fakestorechallenge.utils.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  [LoginViewModel] is a ViewModel that handles the login screen.
 *  @param useCase is a FakeStoreUseCase object that contains the use case.
 *  @param tokenManager is a TokenManager object that contains the token manager.
 *  @property _uiState is a MutableStateFlow object that contains the UI state.
 *  @property uiState is a StateFlow object that contains the UI state.
 *  @property username is a MutableStateFlow object that contains the username.
 *  @property password is a MutableStateFlow object that contains the password.
 *  @property _isLoginEnabled is a MutableStateFlow object that contains the login enabled state.
 *  @property isLoginEnabled is a LiveData object that contains the login enabled state.
 * */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: FakeStoreUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    val username: MutableStateFlow<String> = MutableStateFlow("")
    val password: MutableStateFlow<String> = MutableStateFlow("")

    private val _isLoginEnabled = MutableStateFlow(false)
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled.asStateFlow().asLiveData()

    init {
        validateForm()
    }

    /**
     *  [onUsernameChanged] is a function that handles the username change.
     *  calls [validateForm] to validate the form.
     * */
    fun onUsernameChanged(newUsername: String) {
        username.value = newUsername
        validateForm()
    }

    /**
     *  [onPasswordChanged] is a function that handles the password change.
     *  calls [validateForm] to validate the form.
     * */
    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
        validateForm()
    }

    /**
     *  [validateForm] is a function that validates the form.
     *  combines the username and password flows to validate the form.
     *  updates the uiState with the new values.
     *  enables the login button if the form is valid.
     * */
    private fun validateForm() {
        viewModelScope.launch {
            combine(username, password) { username, password ->
                val isUsernameValid = if (username.contains("@")) {
                    Patterns.EMAIL_ADDRESS.matcher(username).matches()
                } else {
                    username.isNotBlank()
                }
                val isPasswordValid =
                    password.isNotBlank() && password.trim().length >= 6
                _uiState.update {
                    it.copy(
                        username = username,
                        password = password,
                        isUsernameValid = isUsernameValid,
                        isPasswordValid = isPasswordValid
                    )
                }
                isUsernameValid && isPasswordValid
            }.collect { isValid ->
                _isLoginEnabled.update { isValid }
            }
        }
    }

    /**
     *  [login] is a function that handles the login.
     * */
    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isSuccess = false, errorMessage = null) }
            when (val result = useCase.login(LoginRequest(username.value, password.value))) {
                is Result.Success -> {
                    tokenManager.saveToken(result.data.token)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.localizedMessage
                        )
                    }
                }

            }
        }
    }

    /**
     *  [clearErrorMessage] is a function that clears the error message.
     * */
    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}