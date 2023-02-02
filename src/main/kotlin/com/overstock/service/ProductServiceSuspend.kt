package com.overstock.service

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
//import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductServiceSuspend {
    @GET("/product/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): Response<Product>

    @GET("/searchItem/{searchTerm}")
    suspend fun getSearchItem(
        @Path("searchTerm") searchTerm: String
    ): Response<SearchItem>

}

fun createProductServiceWithSuspend(): ProductServiceSuspend {
    val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .header("Accept", "application/json")
            val request = builder.build()
            chain.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8080")
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(ProductServiceSuspend::class.java)
}

