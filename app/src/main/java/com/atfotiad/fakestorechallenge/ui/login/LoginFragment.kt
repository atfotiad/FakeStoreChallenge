package com.atfotiad.fakestorechallenge.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.atfotiad.fakestorechallenge.R
import com.atfotiad.fakestorechallenge.databinding.FragmentLoginBinding
import com.atfotiad.fakestorechallenge.security.TokenManager
import com.atfotiad.fakestorechallenge.utils.ui.viewDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    private val binding by viewDataBinding(FragmentLoginBinding::inflate)

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.loginButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(username, password)

        }
        lifecycleScope.launch {
            viewLifecycleOwner
                .repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loginState.collect { state ->
                        when (state) {
                            is LoginState.Idle -> {
                                handleIdleState()
                            }

                            is LoginState.Loading -> {
                                handleLoadingState()
                            }

                            is LoginState.Success -> {
                                handleSuccessState(state.token)
                            }

                            is LoginState.Error -> {
                                handleErrorState(state.exception)
                            }
                        }
                    }
                }
        }
    }

    private fun handleIdleState() {
        // Reset UI elements to their default state
        binding.username.isEnabled = true
        binding.password.isEnabled = true
        //binding.loginButton.isEnabled = true
        binding.progressBar.visibility = View.GONE
        //binding.errorTextView.visibility = View.GONE
        //binding.errorTextView.text = ""
        // Clear any previous input
        binding.username.text.clear()
        binding.password.text.clear()
        // Optionally, you can set focus to the username field
        binding.username.requestFocus()
    }

    private fun handleLoadingState() {
        // Disable input fields and the login button
        binding.username.isEnabled = false
        binding.password.isEnabled = false
        binding.loginButton.isEnabled = false
        // Show the progress bar
        binding.progressBar.visibility = View.VISIBLE
        // Hide the error message
        //binding.errorTextView.visibility = View.GONE
        //binding.errorTextView.text = ""
    }

    private fun handleSuccessState(token: String) {

        lifecycleScope.launch {
            tokenManager.saveToken(token)
        }
        findNavController().navigate(R.id.homeFragment)
        viewModel.resetState()
    }

    private fun handleErrorState(exception: Exception) {
        // Enable input fields and the login button
        binding.username.isEnabled = true
        binding.password.isEnabled = true
        binding.loginButton.isEnabled = true
        // Hide the progress bar
        binding.progressBar.visibility = View.GONE
        // Show the error message
        //binding.errorTextView.visibility = View.VISIBLE
        //binding.errorTextView.text = exception.message ?: "An unknown error occurred"
    }

}