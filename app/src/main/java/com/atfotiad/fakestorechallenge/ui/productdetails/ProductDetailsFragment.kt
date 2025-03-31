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
import androidx.navigation.fragment.navArgs
import com.atfotiad.fakestorechallenge.data.model.product.Product
import com.atfotiad.fakestorechallenge.databinding.FragmentProductDetailsBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 *  [ProductDetailsFragment] is a Fragment that displays the details of a product.
 *  @property binding is a FragmentProductDetailsBinding object that contains the binding for the view.
 *  @property viewModel is a ProductDetailsViewModel object that contains the ViewModel.
 *  @property args is a ProductDetailsFragmentArgs object that contains the arguments for the fragment.
 * */
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

    /**
     *  [observeUiState] is a function that observes the UI state.
     *  binds the product to the views.
     * */
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

    @SuppressLint("SetTextI18n")
    private fun bindProduct(product: Product) {
        binding.apply {
            productTitle.text = product.title
            productPrice.text = NumberFormat.getCurrencyInstance().format(product.price)
            productDescription.text = product.description
            Glide.with(binding.root).load(product.imageUrl).into(productImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}