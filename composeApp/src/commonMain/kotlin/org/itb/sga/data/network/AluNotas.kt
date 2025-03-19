package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluNotas(
    val asignatura: String,
    val nota: Double,
    val asistencia: Double,
    val fecha: String,
    val convalidacion: Boolean,
    val estado: String
)