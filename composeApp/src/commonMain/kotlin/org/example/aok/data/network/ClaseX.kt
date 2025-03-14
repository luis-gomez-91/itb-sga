package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ClaseX(
    val idLeccionGrupo: Int,
    val abierta: Boolean,
    val asignatura: String,
    val asistenciaCantidad: String,
    val asistenciaProciento: String,
    val aula: String,
    val fecha: String,
    val grupo: String,
    val horaEntrada: String,
    val horaSalida: String,
    val turno: String,
    val turnoTipo: String
)