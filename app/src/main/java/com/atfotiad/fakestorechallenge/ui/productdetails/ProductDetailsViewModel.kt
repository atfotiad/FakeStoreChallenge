package com.atfotiad.fakestorechallenge.ui.productdetails

import androidx.lifecycle.ViewModel
import com.atfotiad.fakestorechallenge.data.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 *  [ProductDetailsViewModel] is a ViewModel that handles the product details screen.
 *  @property _uiState is a MutableStateFlow object that contains the UI state.
 *  @property uiState is a StateFlow object that contains the UI state.
 *  @property setProduct is a function that sets the product.
 * */
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun setProduct(product: Product) {
        _uiState.update { it.copy(product = product) }
    }
}