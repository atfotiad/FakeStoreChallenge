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

    fun onUsernameChanged(newUsername: String) {
        username.value = newUsername
        validateForm()
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
        validateForm()
    }

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

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

}