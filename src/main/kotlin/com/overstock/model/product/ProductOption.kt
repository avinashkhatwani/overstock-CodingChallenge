package com.overstock.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductOption(
    val id: Int,
    val name: String,
    val price: Double ){

}