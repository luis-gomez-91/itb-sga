package org.itb.sga.data.network.pro_evaluaciones

import kotlinx.serialization.Serializable

@Serializable
data class ProEvaluacionesCalificacion(
    val alumno: String,
    val asistencia: Double,
    val estado: String,
    val examen: Int,
    val idMateriaAsignada: Int,
    val n1: Int,
    val n2: Int,
    val n3: Int,
    val n4: Int,
    val notafinal: Int,
    val observaciones: String?,
    val recuperacion: Int
)