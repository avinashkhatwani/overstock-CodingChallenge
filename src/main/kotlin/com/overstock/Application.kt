package com.overstock

import com.overstock.model.combineProductList
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import com.overstock.service.createProductService
import com.overstock.task.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*

fun main(args: Array<String>) {
    runBlocking {
        val server = embeddedServer(Netty, 8080) {
            routing {

                route(SearchItem.path) {
                    get("{searchTerm}") {
                        val searchTerm =
                            call.parameters["searchTerm"]
                                ?: throw IllegalArgumentException("SearchTerm parameter is missing")
                        val jsonString =
                            this.javaClass.classLoader.getResource("searchProducts-$searchTerm.json")?.readText()
                        if (jsonString != null) {
                            call.respondText(jsonString, ContentType.Application.Json)
                        }
                    }

                }

                route(Product.path) {
                    get("/{id}") {
                        Thread.sleep(2000)
                        val id = call.parameters["id"] ?: throw IllegalArgumentException("ID parameter is missing")
                        val jsonString = this.javaClass.classLoader.getResource("product$id.json")?.readText()
                        if (jsonString != null) {
                            call.respondText(jsonString, ContentType.Application.Json)
                        }
                    }

                }

                route(CombinedResult.path) {
                    get("/blocking/{searchTerm}") {
                        val startTime = System.currentTimeMillis()

                        val searchTerm =
                            call.parameters["searchTerm"]
                                ?: throw IllegalArgumentException("SearchTerm parameter is missing")
                        val service = createProductService(searchTerm)
                        val products = loadProductsBlocking(service, searchTerm)

                        val endTime = System.currentTimeMillis()
                        val totalTime = endTime - startTime
                        log("Total time taken: $totalTime ms")

                        call.respondText(products.toString(), ContentType.Application.Json)
                    }

                    get("/suspend/{searchTerm}") {
                        val startTime = System.currentTimeMillis()

                        val searchTerm = call.parameters["searchTerm"]
                            ?: throw IllegalArgumentException("SearchTerm parameter is missing")
                        val service = createProductServiceSuspend(searchTerm)

                        val deferred: Deferred<List<Product>> = async {
                            val products = loadCombinedProductSuspend1(service, searchTerm)
                            log("Received combined product from suspend: $products")
                            products
                        }
                        val products: List<Product> = deferred.await()
                        val combinedResult = combineProductList(products, searchTerm);

                        val endTime = System.currentTimeMillis()
                        val totalTime = endTime - startTime
                        log("Total time taken: $totalTime ms")

                        call.respondText(combinedResult.toString(), ContentType.Application.Json)
                    }

                    get("/concurrent/{searchTerm}") {
                        val startTime = System.currentTimeMillis()
                        val searchTerm = call.parameters["searchTerm"]
                            ?: throw IllegalArgumentException("SearchTerm parameter is missing")
                        val service = createProductServiceSuspend(searchTerm)

                        val products = loadProductsConcurrent(service, searchTerm)
                        log("Received products from Concurrent: $products")
                        val combinedResult = combineProductList(products, searchTerm);

                        val endTime = System.currentTimeMillis()
                        val totalTime = endTime - startTime
                        log("Total time taken: $totalTime ms")

                        call.respondText(combinedResult.toString(), ContentType.Application.Json)

                    }

                }





//                get("/combinedApi/suspend/{searchTerm}") {
//                    val searchTerm = call.parameters["searchTerm"]
//                        ?: throw IllegalArgumentException("SearchTerm parameter is missing")
//                    val service = createProductServiceSuspend(searchTerm)
//
//                    val deferreds: Deferred<List<Product>> = async {
//                        val products = loadCombinedProductSuspend1(service, searchTerm)
//                        log("Received combined product from suspend: $products")
//                        products
//                    }
//                    val products: List<Product> = deferreds.await()
//                    val combinedResult = combineProductList(products, searchTerm);
//                    call.respondText(combinedResult.toString(), ContentType.Application.Json)
//                }

//                get("/combinedApi/concurrent/{searchTerm}") {
//                    val searchTerm = call.parameters["searchTerm"]
//                        ?: throw IllegalArgumentException("SearchTerm parameter is missing")
//                    val service = createProductServiceSuspend(searchTerm)
//
//                    val products = loadProductsConcurrent(service, searchTerm)
//                    log("Received products from Concurrent: $products")
//                    val combinedResult = combineProductList(products, searchTerm);
//                    call.respondText(combinedResult.toString(), ContentType.Application.Json)
//
//                }


            }
        }
        server.start(wait = true)
    }
}