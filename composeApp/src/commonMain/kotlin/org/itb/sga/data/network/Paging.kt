package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Paging(
    val firstPage: Int,
    val lastPage: Int
)