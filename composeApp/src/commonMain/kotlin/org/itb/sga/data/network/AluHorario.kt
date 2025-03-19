package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluHorario(
    val clases: List<Clase>,
    val materiaFin: String,
    val materiaInicio: String,
    val materiaNombre: String,
    val nivelMalla: String,
    val paralelo: String
)