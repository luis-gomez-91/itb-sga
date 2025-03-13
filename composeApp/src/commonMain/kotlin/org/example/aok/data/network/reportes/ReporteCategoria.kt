package org.example.aok.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class ReporteCategoria(
    val categoria: String,
    val reportes: List<Reporte>
)