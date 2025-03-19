package org.itb.sga.data.network.pro_clases

import kotlinx.serialization.Serializable

@Serializable
data class Asistencia(
    val alumno: String,
    val asistenciaId: Int,
    val porcentaje: Double,
    val cantidad: Int,
    val asistio: Boolean
)