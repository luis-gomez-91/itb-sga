package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ProHorarioClase(
    val idClase: Int,
    val aula: String,
    val carrera: String,
    val grupo: String,
    val materia: String,
    val materiaDesde: String,
    val materiaHasta: String,
    val nivel: String,
    val turnoComienza: String,
    val turnoTermina: String
)