package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class GrupoModulo(
    val grupo: String,
    val modulos: List<Modulo>
)