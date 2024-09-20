package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val error: String,
)
