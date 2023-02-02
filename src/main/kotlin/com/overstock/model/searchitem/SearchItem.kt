package com.overstock.model.searchitem

import kotlinx.serialization.Serializable

@Serializable
data class SearchItem(
    val searchTerm: String,
    val itemIds: List<Int>
) {
    companion object {
        const val path = "/searchItem"
    }
}