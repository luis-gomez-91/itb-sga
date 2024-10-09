package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class OtraNotaAsignatura(
    val nombre: String,
    val nota1: Int,
    val nota2: Int,
    val nota3: Int,
    val nota4: Int,
    val estado: String
)