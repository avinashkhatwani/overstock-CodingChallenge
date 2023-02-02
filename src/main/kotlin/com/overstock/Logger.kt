package com.overstock

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Response

val log: Logger = LoggerFactory.getLogger("ProductPlatform")

fun log(msg: String?) {
    log.info(msg)
}

fun logProduct(id: Int, response: Response<Product>) {
    val product = response.body()
    if (!response.isSuccessful || product == null) {
        log.error("Failed loading Product for productId:$id with response: '${response.code()}: ${response.message()}'")
    }
    else {
        log.info("productId:$id- loaded product with details $product ")
    }
}

fun logSearchItem(req: String, response: Response<SearchItem>) {
    val searchItem = response.body()
    if (!response.isSuccessful || searchItem == null) {
        log.error("Failed loading searchItem for searchString$req with response '${response.code()}: ${response.message()}'")
    }
    else {
        log.info("searchItem:$req- loaded searchItem with details $searchItem")
    }
}