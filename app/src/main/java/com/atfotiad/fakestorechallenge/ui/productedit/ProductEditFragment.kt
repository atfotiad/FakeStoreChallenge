package com.atfotiad.fakestorechallenge.ui.productedit

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
import com.atfotiad.fakestorechallenge.databinding.FragmentProductEditBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

/**
 *  [ProductEditFragment] is a Fragment that allows the user to edit a product.
 *  @property binding is a FragmentProductEditBinding object that contains the binding for the view.
 *  @property viewModel is a ProductEditViewModel object that contains the ViewModel.
 *  @property args is a ProductEditFragmentArgs object that contains the arguments for the fragment.
 *  @property bindProduct is a function that binds the product to the views.
 *  @property observeUiState is a function that observes the UI state.
 *  @property getUpdatedProduct is a function that returns the updated product.
 * */
@AndroidEntryPoint
class ProductEditFragment : Fragment() {

    private var _binding: FragmentProductEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductEditViewModel by viewModels()
    private val args: ProductEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        bindProduct(product)
        observeUiState()
        binding.saveButton.setOnClickListener {
            val updatedProduct = getUpdatedProduct(product)
            viewModel.updateProduct(updatedProduct)
            val action =
                ProductEditFragmentDirections.actionProductEditFragmentToProductDetailsFragment(
                    product = updatedProduct
                )
            findNavController().navigate(action)
        }
    }

    private fun bindProduct(product: Product) {
        binding.apply {
            productTitle.setText(product.title)
            productPrice.setText(String.format(Locale.getDefault(), product.price.toString()))
            productDescription.setText(product.description)
            productCategory.setText(product.category)
            Glide.with(binding.root).load(product.imageUrl).into(productImage)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.product?.let { bindProduct(it) }
                }
            }
        }
    }

    private fun getUpdatedProduct(product: Product): Product {
        return product.copy(
            title = binding.productTitle.text.toString(),
            price = binding.productPrice.text.toString().toDouble(),
            description = binding.productDescription.text.toString(),
            category = binding.productCategory.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}