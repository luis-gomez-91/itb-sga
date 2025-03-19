package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val status: String,
    val message: String
)