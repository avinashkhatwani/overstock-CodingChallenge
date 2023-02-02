package com.overstock.task

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import com.overstock.ProductServiceSuspended
import com.overstock.logProduct
import com.overstock.logSearchItem
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta

suspend fun loadCombinedProductSuspend(service: ProductServiceSuspended, req: String): CombinedResult {
    val searchProduct = service
        .getSearchItem(req)
        .also { logSearchItem(req, it) }
        .body() ?: SearchItem("", emptyList())

    val products: List<Product> = searchProduct.itemIds.mapNotNull { id ->
        val response = service.getProduct(id)
            .also { logProduct(id, it) }
        if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

    val modifiedProductList = products.map { product ->
        val productOptions = product.options
        val priceRange = productOptions.map { it.price }.let { "${it.min()} - ${it.max()}" }
        product.copy(priceRange = priceRange)
    }

    return CombinedResult(Meta(req, modifiedProductList.size), modifiedProductList);
}

suspend fun loadCombinedProductSuspend1(service: ProductServiceSuspended, req: String): List<Product> {
    val searchProduct = service
        .getSearchItem(req)
        .also { logSearchItem(req, it) }
        .body() ?: SearchItem("", emptyList())

    val products: List<Product> = searchProduct.itemIds.mapNotNull { id ->
        val response = service.getProduct(id)
            .also { logProduct(id, it) }
        if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

//    val modifiedProductList = products.map { product ->
//        val productOptions = product.options
//        val priceRange = productOptions.map { it.price }.let { "${it.min()} - ${it.max()}" }
//        product.copy(priceRange = priceRange)
//    }
//
////    return CombinedResult(Meta(req, modifiedProductList.size), modifiedProductList);

    return products;
}