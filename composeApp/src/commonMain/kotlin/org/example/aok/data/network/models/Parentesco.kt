package org.example.aok.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Parentesco(
    val nombre: String,
    val id: Int
)