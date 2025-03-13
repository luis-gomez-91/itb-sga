package org.example.aok.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class Reporte(
    val descripcion: String,
    val id: Int,
    val nombre: String
)