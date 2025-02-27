package com.atfotiad.fakestorechallenge.data.model.product
/** [HomeData] is a data class that holds the data for the home screen.
 *  @property products is a list of products.
 *  @property categories is a list of categories.
 *  @property selectedCategory is the selected category.
 *  @property allProducts is a list of all products.
 * */
data class HomeData(
    val products: List<Product>,
    val categories: List<String>,
    val selectedCategory: String? = null,
    val allProducts: List<Product> = products
)
