package com.atfotiad.fakestorechallenge.ui.productdetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.databinding.FragmentProductDetailsBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductDetailsViewModel by viewModels()
    private val args: ProductDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        observeUiState()
        viewModel.setProduct(product)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.product?.let {
                        bindProduct(it)
                    }
                }
            }
        }
    }

    private fun navigateToEditProduct(product: Product) {
        val action =
            ProductDetailsFragmentDirections.actionProductDetailsFragmentToProductEditFragment(
                product
            )
        findNavController().navigate(action)
    }

    @SuppressLint("SetTextI18n")
    private fun bindProduct(product: Product) {
        binding.apply {
            productTitle.text = product.title
            productPrice.text = product.price.toString()
            productDescription.text = product.description
            Glide.with(binding.root).load(product.imageUrl).into(productImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}