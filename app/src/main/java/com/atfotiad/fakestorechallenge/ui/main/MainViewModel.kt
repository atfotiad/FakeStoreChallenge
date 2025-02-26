package com.atfotiad.fakestorechallenge.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.fakestorechallenge.security.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _isLoggedIn = MutableSharedFlow<Boolean>(replay = 1)
    val isLoggedIn: SharedFlow<Boolean> = _isLoggedIn.asSharedFlow()

    init {
        Log.d("MainViewModel", "MainViewModel instance created: $this")
        checkToken()
    }

    private fun checkToken() {
        viewModelScope.launch {
            Log.d("MainViewModel", "checkToken: Checking for token")
            tokenManager.getToken().collect { token ->
                Log.d("MainViewModel", "checkToken: Token: $token")
                val hasToken = tokenManager.hasToken()
                Log.d("MainViewModel", "checkToken: hasToken: $hasToken")
                _isLoggedIn.emit(hasToken)
                Log.d("MainViewModel", "checkToken: isLoggedIn: $hasToken")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            Log.d("MainViewModel", "logout: Logging out")
            tokenManager.deleteToken()
            _isLoggedIn.emit(false)
            Log.d("MainViewModel", "logout: isLoggedIn: false")
        }
    }
}