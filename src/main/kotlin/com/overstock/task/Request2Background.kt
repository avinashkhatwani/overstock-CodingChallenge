package com.overstock.task

import com.overstock.ProductService
import com.overstock.model.combinedResults.CombinedResult
import kotlin.concurrent.thread


fun loadCombinedResultBackground(service: ProductService, req: String, updateResults: (List<CombinedResult>) -> Unit) {
    thread {
        loadContributorsBlocking(service, req)
    }
}