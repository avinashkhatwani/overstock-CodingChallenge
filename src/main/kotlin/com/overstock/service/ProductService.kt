package com.overstock.service

import com.overstock.model.product.Product
import com.overstock.model.searchitem.SearchItem
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("/product/{id}")
    fun getProductCall(
        @Path("id") id: Int
    ): Call<Product>

    @GET("/searchItem/{searchTerm}")
    fun getSearchItemCall(
        @Path("searchTerm") searchTerm: String
    ): Call<SearchItem>

}

fun createProductService(): ProductService {
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
    return retrofit.create(ProductService::class.java)
}

