package com.overstock.model.combinedResults

import com.overstock.model.product.Product
import com.overstock.model.product.ProductOption
import kotlinx.serialization.Serializable


@Serializable
data class CombinedResult(
    val meta: Meta,
    val items: List<Product>) {

    companion object {
        const val path = "/combinedResult"
    }
}

@Serializable
data class Meta(
    val searchTerm: String,
    val count: Int
)