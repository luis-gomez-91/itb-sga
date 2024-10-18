package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ProHorario(
    val clases: List<ProHorarioClase>,
    val diaId: Int,
    val diaNombre: String
)