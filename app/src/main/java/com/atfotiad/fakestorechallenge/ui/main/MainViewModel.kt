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

/**
 *  [MainViewModel] is a ViewModel that handles the main screen.
 *  @param tokenManager is a TokenManager object that contains the token manager.
 *  @property _isLoggedIn is a MutableSharedFlow object that contains the login state.
 *  @property isLoggedIn is a SharedFlow object that contains the login state.
 * */
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

    /**
     *  [checkToken] is a function that checks the token.
     * */
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

    /**
     *  [logout] is a function that handles the logout.
     *  Currently it only deletes the token.
     * */
    fun logout() {
        viewModelScope.launch {
            Log.d("MainViewModel", "logout: Logging out")
            tokenManager.deleteToken()
            _isLoggedIn.emit(false)
            Log.d("MainViewModel", "logout: isLoggedIn: false")
        }
    }
}