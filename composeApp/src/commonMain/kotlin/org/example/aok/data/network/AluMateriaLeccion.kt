package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluMateriaLeccion(
    val asistio: Boolean,
    val fecha: String,
    val horaEntrada: String,
    val horaSalida: String
)