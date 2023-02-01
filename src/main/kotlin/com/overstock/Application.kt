package com.overstock

import com.overstock.model.combineProductList
import com.overstock.model.combinedResults.CombinedResult
import com.overstock.model.combinedResults.Meta
import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.overstock.task.loadCombinedResultBackground
import com.overstock.task.loadContributorsBlocking
import com.overstock.task.loadCombinedProductSuspend
import com.overstock.task.loadContributorsConcurrent
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//fun main() {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}
//
//fun Application.module() {
//    configureSerialization()
//    configureRouting()
//}

//@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)

fun main(args: Array<String>) {
    runBlocking {
        embeddedServer(
            Netty,
            watchPaths = listOf("com.overstock"),
            port = 8080,
            host = "0.0.0.0",
            module = Application::module
        ).start(wait = true)
    }
}

fun Application.module() {
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")

    routing {

        route(SearchItem.path) {
            get("{searchTerm}") {
                val searchTerm =
                    call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
                val jsonString = this.javaClass.classLoader.getResource("searchProducts-$searchTerm.json")?.readText()
                if (jsonString != null) {
                    call.respondText(jsonString, ContentType.Application.Json)
                }
            }

        }

        route(Product.path) {
            get("/{id}") {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("ID parameter is missing")
                val jsonString = this.javaClass.classLoader.getResource("product$id.json")?.readText()
                if (jsonString != null) {
                    call.respondText(jsonString, ContentType.Application.Json)
                }
            }

        }

//        get("/"){
//            call.respond(Customer(1, "A","B"))
//        }

//        get("/{id}") {
//            val id = call.parameters["id"] ?: throw IllegalArgumentException("ID parameter is missing")
//            val jsonString = this.javaClass.classLoader.getResource("product$id.json")?.readText()
//            if (jsonString != null) {
//                call.respondText(jsonString, ContentType.Application.Json)
//            }
//        }
//
//        get("/searchapi/{searchTerm}") {
//            val searchTerm = call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
//            val jsonString = this.javaClass.classLoader.getResource("searchProducts-$searchTerm.json")?.readText()
//            if (jsonString != null) {
//                call.respondText(jsonString, ContentType.Application.Json)
//            }
//        }

        get("/combinedApi/blocking/{searchTerm}") {
            val service = createProductService("Modern")
            val searchTerm =
                call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
            val products = loadContributorsBlocking(service, searchTerm)
            call.respondText(products.toString(), ContentType.Application.Json)
        }

        get("/combinedApi/backgroundThread/{searchTerm}") {
            val service = createProductService("Modern")
            val searchTerm =
                call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
            loadCombinedResultBackground(service, searchTerm) { products ->
                println("Received combined product from backend: $products")
            }
            call.respondText("Contents have been loaded", ContentType.Application.Json)

        }

        get("/combinedApi/suspend/{searchTerm}") {
            val searchTerm =
                call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
            val service = createProductServiceSuspend(searchTerm)

            launch {
                val products = loadCombinedProductSuspend(service, searchTerm)
                println("Received combined product from suspend: $products")
                call.respondText(products.toString(), ContentType.Application.Json)
            }

        }

        get("/combinedApi/concurrent/{searchTerm}") {
            val searchTerm =
                call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
            val service = createProductServiceSuspend(searchTerm)

            val products = loadContributorsConcurrent(service, searchTerm)
            println("Received products from suspend: $products")
            val combinedResult = combineProductList(products, searchTerm);
            call.respondText(combinedResult.toString(), ContentType.Application.Json)

        }


    }


}