package com.overstock.task


import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import com.overstock.service.ProductService
import com.overstock.logProduct
import com.overstock.logSearchItem
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta

fun loadProductsBlocking(service: ProductService, req: String) : List<Product> {
    val searchProduct = service
        .getSearchItemCall(req)
        .execute() // Executes request and blocks the current thread
        .also { logSearchItem(req, it) }
        .body() ?: SearchItem("", emptyList())

    val products: List<Product> = searchProduct.itemIds.mapNotNull { id ->
        val response = service.getProductCall(id).execute()
            .also { logProduct(id, it) }
        if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }.toList()

    val modifiedProductList = products.map { product ->
        val productOptions = product.options
        val priceRange = productOptions.map { it.price }.let { "${it.min()} - ${it.max()}" }
        product.copy(priceRange = priceRange)
    }.toList()

    val combinedResult = CombinedResult(Meta(req, modifiedProductList.size), modifiedProductList)

    return products;
}