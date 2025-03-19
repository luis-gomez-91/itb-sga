package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class RubroX(
    val id: Int,
    val fecha: String,
    val nombre: String,
    val valor: Double
)