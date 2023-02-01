package com.overstock.task


import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import com.overstock.ProductService
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta
import retrofit2.Response

fun loadContributorsBlocking(service: ProductService, req: String) : CombinedResult {
    val searchProduct = service
        .getSearchItemCall(req)
        .execute() // Executes request and blocks the current thread
//        .also { logRepos(req, it) }
        .body() ?: SearchItem("", emptyList())
    val productIds = searchProduct.itemIds

    //WORKING!
    var products1: List<Product>  = searchProduct.itemIds.map { id ->
        service.getProductCall(id)
            .execute() // Executes request and blocks the current thread
//            .also { logUser(repo, it) }
            .body()?: Product(0, "", "", emptyList())
    }

    var products: List<Product> = searchProduct.itemIds.mapNotNull { id ->
        val response = service.getProductCall(id).execute()
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

    return combinedResult;
}