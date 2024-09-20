package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Periodo(
    val desde: String,
    val hasta: String,
    val id: Int,
    val nombre: String
)