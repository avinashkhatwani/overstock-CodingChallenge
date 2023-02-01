package com.overstock.task

import com.overstock.ProductServiceSuspended
import com.overstock.logProduct
import com.overstock.logSearchItem
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta
import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import kotlinx.coroutines.*

class Request5Concurrent {
}
suspend fun loadContributorsConcurrent(service: ProductServiceSuspended, req: String): List<Product?> = coroutineScope  {
    val searchProduct = service
        .getSearchItem(req)
        .also { logSearchItem(req, it) }
        .body() ?: SearchItem("", emptyList())

    val deferreds: List<Deferred<Product?>> = searchProduct.itemIds.map { id ->
        async(Dispatchers.Default) {
            val response = service.getProduct(id)
                .also { logProduct(req, it) }
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                null
            }
        }
    }
    deferreds.awaitAll().filterNotNull();
}
