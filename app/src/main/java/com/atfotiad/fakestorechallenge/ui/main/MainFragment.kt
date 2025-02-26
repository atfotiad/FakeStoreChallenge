package com.atfotiad.fakestorechallenge.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.atfotiad.fakestorechallenge.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/** An empty fragment responsible for
 * checking the token state and navigating to
 * [@LoginFragment] or [@HomeFragment]
 * */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_main,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoggedIn.collect { isLoggedIn ->

                    val destination = if (isLoggedIn) {
                        // Navigate to HomeFragment
                        R.id.homeFragment
                    } else {
                        // Navigate to LoginFragment
                        R.id.loginFragment
                    }

                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.mainFragment, true)
                        .build()
                    findNavController().navigate(destination, null, navOptions)
                }
            }
        }
    }
}