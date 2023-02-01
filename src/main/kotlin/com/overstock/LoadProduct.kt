package com.overstock

import com.overstock.model.combinedResults.CombinedResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

interface LoadProduct: CoroutineScope {
    val job: Job

    private enum class LoadingStatus { COMPLETED, CANCELED, IN_PROGRESS }

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

    private fun updateResults(
        combinedResults: List<CombinedResult>,
        startTime: Long,
        completed: Boolean = true
    ) {
//        updateCombinedResult(combinedResults)
        updateLoadingStatus(if (completed) LoadingStatus.COMPLETED else LoadingStatus.IN_PROGRESS, startTime)
        if (completed) {
            setActionsStatus(newLoadingEnabled = true)
        }
    }

    fun loadProducts() {
        val req = "Modern"
        val service = createProductService(req);

    }

    private fun updateLoadingStatus(
        status: LoadingStatus,
        startTime: Long? = null
    ) {
        val time = if (startTime != null) {
            val time = System.currentTimeMillis() - startTime
            "${(time / 1000)}.${time % 1000 / 100} sec"
        } else ""

        val text = "Loading status: " +
                when (status) {
                    LoadingStatus.COMPLETED -> "completed in $time"
                    LoadingStatus.IN_PROGRESS -> "in progress $time"
                    LoadingStatus.CANCELED -> "canceled"
                }
        setLoadingStatus(text, status == LoadingStatus.IN_PROGRESS)
    }

    fun setLoadingStatus(text: String, iconRunning: Boolean)

    fun updateCombinedResult(combinedResults: List<CombinedResult>)

    fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false)
    fun addLoadListener(listener: () -> Unit)
}