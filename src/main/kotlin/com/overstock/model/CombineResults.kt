package com.overstock.model

import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta
import com.overstock.model.product.Product

fun combineProductList(products: List<Product?>, searchTerm: String): CombinedResult {
    val modifiedProductList = products.mapNotNull { product ->
        val productOptions = product?.options
        val priceRange = productOptions?.map { it.price }?.let { "${it.min()} - ${it.max()}" }
        if (priceRange != null && product != null) {
            product.copy(priceRange = priceRange)
        } else {
            null
        }
    }

    return CombinedResult(Meta(searchTerm, modifiedProductList.size), modifiedProductList);
}