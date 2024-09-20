package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class PagingBack(
    val from: Int,
    val to: Int

)