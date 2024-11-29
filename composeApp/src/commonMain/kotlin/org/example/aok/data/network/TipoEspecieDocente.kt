package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class TipoEspecieDocente(
    val id: Int,
    val nombre: String
)