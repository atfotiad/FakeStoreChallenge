package com.atfotiad.fakestorechallenge.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.atfotiad.fakestorechallenge.data.model.product.HomeUiState
import com.atfotiad.fakestorechallenge.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productLayoutManager: LinearLayoutManager
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeUiState()
    }

    private fun setupRecyclerViews() {
        categoryAdapter = CategoryAdapter { category ->
            viewModel.selectCategory(category)
        }
        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = categoryAdapter
        }

        productAdapter = ProductAdapter()
        binding.productsRecyclerView.apply {
            productLayoutManager = GridLayoutManager(
                context,
                2,
                GridLayoutManager.HORIZONTAL,
                false
            )
            layoutManager = productLayoutManager
            adapter = productAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is HomeUiState.Loading -> {
                            // Show loading indicator
                        }

                        is HomeUiState.Success -> {
                            categoryAdapter.submitList(uiState.data.categories)
                            productAdapter.submitList(uiState.data.products)
                            categoryAdapter.selectedCategory = uiState.data.selectedCategory
                            productLayoutManager.scrollToPosition(0)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}