package com.overstock.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val priceRange: String,
    val options: List<ProductOption>
) {
    companion object {
        const val path = "/product"
    }
}

@Serializable
data class ProductOption(
    val id: Int,
    val name: String,
    val price: Double
)