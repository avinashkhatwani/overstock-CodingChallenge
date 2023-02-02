package com.overstock.task


//fun loadCombinedResultBackground(service: ProductService, req: String, updateResult: (List<Product>) -> Unit) {
//    thread {
//        updateResult(loadContributorsBlocking(service, req))
//    }
//}
//
//fun loadCombinedResultBackground1(service: ProductService, req: String, updateResult: (List<Product>) -> CombinedResult) {
//    thread {
//        val result = loadContributorsBlocking(service, req)
//        updateResult(result)
//    }
//}