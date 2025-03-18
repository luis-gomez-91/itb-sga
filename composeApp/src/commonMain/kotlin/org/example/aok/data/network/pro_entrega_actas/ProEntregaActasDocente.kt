package org.example.aok.data.network.pro_entrega_actas

import kotlinx.serialization.Serializable

@Serializable
data class ProEntregaActasDocente(
    val docente: String,
    val fechaDesde: String,
    val fechaHasta: String,
    val segmento: String
)