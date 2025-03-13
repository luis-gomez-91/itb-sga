package org.example.aok.data.network.pro_clases

import kotlinx.serialization.Serializable

@Serializable
data class Asistencia(
    val alumno: String,
    val asistenciaId: Int
)