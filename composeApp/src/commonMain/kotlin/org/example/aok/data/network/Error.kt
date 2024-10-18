package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    var title: String,
    val error: String
)
