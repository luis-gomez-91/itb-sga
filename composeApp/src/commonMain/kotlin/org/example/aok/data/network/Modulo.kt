package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Modulo(
    val descripcion: String,
    val img: String,
    val nombre: String,
    val url: String
)