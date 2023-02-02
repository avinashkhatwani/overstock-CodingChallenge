package com.overstock.service.loadproducts

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import com.overstock.service.ProductServiceSuspend
import com.overstock.logProduct
import com.overstock.logSearchItem

suspend fun loadProductUsingSuspend(service: ProductServiceSuspend, req: String): List<Product> {
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

    return products;
}
