package com.atfotiad.fakestorechallenge.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.atfotiad.fakestorechallenge.R
import com.atfotiad.fakestorechallenge.databinding.FragmentLoginBinding
import com.atfotiad.fakestorechallenge.utils.ui.viewDataBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    private val binding: FragmentLoginBinding by viewDataBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeUiState()
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.isSuccess) {
                        // Navigate to the next screen
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.loginFragment, true)
                            .build()
                        findNavController().navigate(R.id.homeFragment, null, navOptions)
                    }
                    if (uiState.errorMessage != null) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Login Error")
                            .setMessage(uiState.errorMessage)
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                viewModel.clearErrorMessage()
                            }.create().apply { setCanceledOnTouchOutside(false) }
                            .show()
                    }
                }
            }
        }
    }
}