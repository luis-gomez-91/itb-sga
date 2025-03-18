package org.example.aok.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class ReporteParametro(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val tipo: Int,
    val modelo: String?
)