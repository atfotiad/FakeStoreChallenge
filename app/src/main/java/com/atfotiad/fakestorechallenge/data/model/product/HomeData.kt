package com.atfotiad.fakestorechallenge.data.model.product

data class HomeData(
    val products: List<Product>,
    val categories: List<String>,
    val selectedCategory: String? = null,
    val allProducts: List<Product> = products
)
