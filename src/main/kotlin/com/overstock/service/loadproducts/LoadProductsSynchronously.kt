package com.overstock.service.loadproducts


import com.overstock.model.product.Product
import com.overstock.service.ProductService
import com.overstock.logProduct
import com.overstock.logSearchItem
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta

fun loadProductsSynchronously(service: ProductService, req: String): CombinedResult {
    val searchProduct = service
        .getSearchItemCall(req)
        .execute() // Executes request and blocks the current thread
        .also { logSearchItem(req, it) }
        .body() ?: return CombinedResult(Meta(req, 0), emptyList())

    val products: List<Product> = searchProduct.itemIds.mapNotNull { id ->
        val response = service.getProductCall(id).execute()
            .also { logProduct(id, it) }
        if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

    val modifiedProductList = products.map { product ->
        val priceRange = product.options.map { it.price }.let { "${it.min()} - ${it.max()}" }
        product.copy(priceRange = priceRange)
    }

    return CombinedResult(Meta(req, modifiedProductList.size), modifiedProductList);
}