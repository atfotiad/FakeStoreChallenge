package com.atfotiad.fakestorechallenge.ui.productdetails

import androidx.lifecycle.ViewModel
import com.atfotiad.fakestorechallenge.data.model.product.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun setProduct(product: Product) {
        _uiState.update { it.copy(product = product) }
    }
}