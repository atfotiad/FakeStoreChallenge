package com.atfotiad.fakestorechallenge.ui.productdetails

import com.atfotiad.fakestorechallenge.data.model.product.Product

/**
 *  [ProductDetailsUiState] is a data class that contains the UI state
 *  for the product details screen.
 * */
data class ProductDetailsUiState(
    val product: Product? = null
)