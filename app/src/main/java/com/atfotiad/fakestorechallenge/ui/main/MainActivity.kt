package com.atfotiad.fakestorechallenge.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.atfotiad.fakestorechallenge.R
import com.atfotiad.fakestorechallenge.databinding.ActivityMainBinding
import com.atfotiad.fakestorechallenge.utils.ui.viewDataBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *  [MainActivity] is a FragmentActivity that contains the main navigation graph.
 *  @property binding is a ActivityMainBinding object that contains the binding for the view.
 *  @property navController is a NavController object that contains the navigation controller.
 *  @property viewModel is a MainViewModel object that contains the shared ViewModel.
 * */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val binding: ActivityMainBinding by viewDataBinding()
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupToolbar()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     *  [setupToolbar] is a function that sets up the toolbar.
     *  handles the toolbar behavior for each destination.
     * */
    private fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarLayout.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, bundle ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.toolbarLayout.toolbar.navigationIcon = null
                    binding.toolbarLayout.toolbarLogo.visibility = View.VISIBLE
                    binding.toolbarLayout.toolbarProfile.visibility = View.VISIBLE
                    binding.toolbarLayout.editImageView.visibility = View.GONE
                    binding.toolbarLayout.toolbarProfile.setOnClickListener {
                        viewModel.logout()
                    }
                }
                R.id.productDetailsFragment -> {
                    binding.toolbarLayout.toolbarLogo.visibility = View.GONE
                    binding.toolbarLayout.toolbarProfile.visibility = View.GONE
                    binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.vector_3)
                    binding.toolbarLayout.editImageView.visibility = View.VISIBLE
                    binding.toolbarLayout.editImageView.setOnClickListener {
                        navController.navigate(R.id.action_productDetailsFragment_to_productEditFragment,bundle)
                    }
                }
                R.id.productEditFragment -> {
                    binding.toolbarLayout.toolbarLogo.visibility = View.GONE
                    binding.toolbarLayout.toolbarProfile.visibility = View.GONE
                    binding.toolbarLayout.editImageView.visibility = View.GONE
                    binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.vector_3)
                }
                else -> {
                    binding.toolbarLayout.toolbar.navigationIcon = null
                    binding.toolbarLayout.toolbarLogo.visibility = View.GONE
                    binding.toolbarLayout.toolbarProfile.visibility = View.GONE
                    binding.toolbarLayout.editImageView.visibility = View.GONE
                }
            }
        }
    }
}