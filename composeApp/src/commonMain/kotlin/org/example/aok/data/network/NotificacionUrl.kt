package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class NotificacionUrl(
    val name: String,
    val tipo: String,
    val title: String,
    val url: String
)