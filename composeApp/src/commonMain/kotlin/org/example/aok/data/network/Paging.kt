package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Paging(
    val back: PagingBack,
    var next: PagingNext,
    val first: Boolean,
    val last: Boolean

)