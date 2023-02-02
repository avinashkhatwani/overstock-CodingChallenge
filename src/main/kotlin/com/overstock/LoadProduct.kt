package com.overstock

import com.overstock.model.combinedResults.CombinedResult
import com.overstock.service.createProductService
//import com.overstock.task.loadCombinedResultBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

enum class Variant {
    BLOCKING,         // Request1Blocking
    BACKGROUND,       // Request2Background
    CALLBACKS,        // Request3Callbacks
    SUSPEND,          // Request4Coroutine
    CONCURRENT,       // Request5Concurrent
    NOT_CANCELLABLE,  // Request6NotCancellable
    PROGRESS,         // Request6Progress
    CHANNELS          // Request7Channels
}




interface LoadProduct : CoroutineScope {
    val job: Job

    private enum class LoadingStatus { COMPLETED, CANCELED, IN_PROGRESS }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun init() {
        // Start a new loading on 'load' click
        addLoadListener {
//            saveParams()
//            loadProducts()
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

    fun loadProducts(req: String) {
        val service = createProductService(req);
        val startTime = System.currentTimeMillis()
        when (getSelectedVariant()) {
            Variant.BACKGROUND -> { // Blocking a background thread
//                loadCombinedResultBackground(service, req) { combinedResult -> Unit{}
//                    updateResults(combinedResult, startTime)
//                }
            }

//            CALLBACKS -> { // Using callbacks
//                loadContributorsCallbacks(service, req) { users ->
//                    SwingUtilities.invokeLater {
//                        updateResults(users, startTime)
//                    }
//                }
//            }

            else -> {
                print("No method!")
            }
        }
    }

    fun getSelectedVariant(): Variant
    fun updateResults(
        combinedResult: CombinedResult,
        startTime: Long,
        completed: Boolean = true
    ) {
        print("Updating results now!");
        updateCombinedResult(combinedResult)
        updateLoadingStatus(if (completed) LoadingStatus.COMPLETED else LoadingStatus.IN_PROGRESS, startTime)
        if (completed) {
            setActionsStatus(newLoadingEnabled = true)
        }
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

    fun updateCombinedResult(combinedResult: CombinedResult)

    fun setActionsStatus(newLoadingEnabled: Boolean, cancellationEnabled: Boolean = false)
    fun addLoadListener(listener: () -> Unit)
}