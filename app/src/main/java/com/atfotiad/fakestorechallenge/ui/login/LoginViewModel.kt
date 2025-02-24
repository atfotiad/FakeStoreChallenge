package com.atfotiad.fakestorechallenge.ui.login

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.atfotiad.fakestorechallenge.usecase.FakeStoreUseCase
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.getOrError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()


    val _username = MutableStateFlow("")
    private val username: StateFlow<String> = _username.asStateFlow()

    val _password = MutableStateFlow("")
    private val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoginEnabled = MutableStateFlow(false)
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled.asStateFlow().asLiveData()

    init {
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
                    password.isNotBlank() && password.trim().length >= 6 // Example: At least 6 characters
                isUsernameValid && isPasswordValid
            }.collect { isValid ->
                _isLoginEnabled.update { isValid }
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val loginResponse = useCase.login(username, password).getOrError(context)
            if (loginResponse != null) {
                _loginState.value = LoginState.Success(loginResponse.token)
            } else {
                _loginState.value = LoginState.Idle
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }

}