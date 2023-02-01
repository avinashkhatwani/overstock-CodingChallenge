package com.overstock

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Response

val log: Logger = LoggerFactory.getLogger("Products")

fun log(msg: String?) {
    log.info(msg)
}

fun logProduct(req: String, response: Response<Product>) {
    val product = response.body()
    if (!response.isSuccessful || product == null) {
        log.error("Failed loading Product for $req with response: '${response.code()}: ${response.message()}'")
    }
    else {
        log.info("$req: loaded $product product")
    }
}

fun logSearchItem(req: String, response: Response<SearchItem>) {
    val searchItem = response.body()
    if (!response.isSuccessful || searchItem == null) {
        log.error("Failed loading searchItem for $req with response '${response.code()}: ${response.message()}'")
    }
    else {
        log.info("$req: loaded $searchItem searchItem deatils")
    }
}