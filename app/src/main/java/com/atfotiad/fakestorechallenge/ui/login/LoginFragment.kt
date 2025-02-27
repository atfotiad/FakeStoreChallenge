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
import com.atfotiad.fakestorechallenge.utils.repository.RepoUtils.getOrError
import com.atfotiad.fakestorechallenge.utils.repository.Result
import com.atfotiad.fakestorechallenge.utils.ui.viewDataBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 *  [LoginFragment] is a Fragment that displays a login screen.
 *  @property viewModel is a LoginViewModel object that contains the ViewModel.
 *  @property binding is a FragmentLoginBinding object that contains the binding for the view.
 * */
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

    /**
     *  [observeUiState] is a function that observes the UI state.
     *  and navigates to the next screen if the login is successful.
     *  or shows error message if the login fails.
     * */
    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if (!uiState.isLoading && uiState.result != Result.Loading ) {
                        if (uiState.result.getOrError(requireContext()) != null) {
                            //navigate to next screen
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build()
                            findNavController().navigate(R.id.homeFragment, null, navOptions)

                       }
                    }
                }
            }
        }
    }
}