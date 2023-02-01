package com.overstock

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

import com.overstock.plugins.*
import com.overstock.task.loadContributorsBlocking
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.print.attribute.standard.Compression

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
    embeddedServer(Netty,
        watchPaths = listOf("com.overstock"),
        port = 8080,
        host = "0.0.0.0",
        module = Application::module)
        .start(wait = true)
}

fun Application.module() {
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")


    routing {

        route(SearchItem.path) {
//            get {
//                call.respond(shoppingList)
//            }
            get("{searchTerm}") {
                val searchTerm = call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
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

        get("/"){
            call.respond(Customer(1, "A","B"))
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("ID parameter is missing")
            val jsonString = this.javaClass.classLoader.getResource("product$id.json")?.readText()
            if (jsonString != null) {
                call.respondText(jsonString, ContentType.Application.Json)
            }
        }

        get("/searchapi/{searchTerm}") {
            val searchTerm = call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
            val jsonString = this.javaClass.classLoader.getResource("searchProducts-$searchTerm.json")?.readText()
            if (jsonString != null) {
                call.respondText(jsonString, ContentType.Application.Json)
            }
        }

        get("/combinedApi/{searchTerm}") {

//            val retrofit = Retrofit.Builder()
//                .baseUrl("http://localhost:8080")
//                .addConverterFactory(MoshiConverterFactory.create())
//                .build()
            val service = createProductService("Modern")
            val searchTerm = call.parameters["searchTerm"] ?: throw IllegalArgumentException("SearchTerm parameter is missing")
            val products = loadContributorsBlocking(service, searchTerm)
            call.respondText(products.toString(), ContentType.Application.Json)
        }

    }

    fun oadContributors() {
        val req = "modern";
//        val service = createProductService()
    }

    install(ContentNegotiation) {
        gson()
    }
}