package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class PagingNext(
    var from: Int = 1,
    var to: Int = 20
)