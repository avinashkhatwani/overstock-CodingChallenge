package com.overstock

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface LoadProduct: CoroutineScope {
    val job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun init() {
        // Start a new loading on 'load' click
        addLoadListener {
//            saveParams()
            loadProducts()
            print("LOAD PRODUCTS!!")
        }

        // Save preferences and exit on closing the window
//        addOnWindowClosingListener {
//            job.cancel()
//            saveParams()
//            exitProcess(0)
//        }

        // Load stored params (user & password values)
//        loadInitialParams()
    }

    fun loadProducts() {
        val req = "Modern"
        val service = createProductService(req);

    }


    fun addLoadListener(listener: () -> Unit)
}