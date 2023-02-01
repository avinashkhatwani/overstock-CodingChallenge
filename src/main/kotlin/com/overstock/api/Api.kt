package com.overstock.api

//class Api {
//}


import com.overstock.model.product.Product
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.server.plugins.contentnegotiation.*

import io.ktor.client.*
import io.ktor.client.engine.cio.*



//suspend fun getProducts(): List<Product> {
//    return jsonClient.get(Product.path).body()
//}

//suspend fun addShoppingListItem(shoppingListItem: ShoppingListItem) {
//    jsonClient.post(ShoppingListItem.path) {
//        contentType(ContentType.Application.Json)
//        setBody(shoppingListItem)
//    }
//}
//
//suspend fun deleteShoppingListItem(shoppingListItem: ShoppingListItem) {
//    jsonClient.delete(ShoppingListItem.path + "/${shoppingListItem.id}")
//}