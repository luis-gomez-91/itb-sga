package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RubroX(
    val id: Int,
    val fecha: String,
    val nombre: String,
    val valor: Double
)