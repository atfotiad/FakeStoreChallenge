package com.atfotiad.fakestorechallenge.data.model.product

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * [Product] is a data class that represents a product.
 * @property id is the id of the product.
 * @property title is the title of the product.
 * @property price is the price of the product.
 * @property description is the description of the product.
 * @property category is the category of the product.
 * @property imageUrl is the url of the image of the product.
 */
@Parcelize
@JsonClass(generateAdapter = true)
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    @Json(name = "image") val imageUrl: String

): Parcelable
