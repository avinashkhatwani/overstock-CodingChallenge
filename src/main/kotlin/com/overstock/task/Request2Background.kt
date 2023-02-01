package com.overstock.task

import com.overstock.ProductService
import com.overstock.model.combinedResults.CombinedResult
import kotlin.concurrent.thread


fun loadCombinedResultBackground(service: ProductService, req: String, updateResult: (CombinedResult) -> Unit) {
    thread {
        updateResult(loadContributorsBlocking(service, req))
    }
}