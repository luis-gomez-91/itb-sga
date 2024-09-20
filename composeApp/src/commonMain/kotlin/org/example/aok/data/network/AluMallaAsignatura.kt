package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluMallaAsignatura(
    val aprobado: Boolean,
    val asignaturaMallaIdent: String,
    val asignaturaNombre: String,
    val asistencia: String,
    val ejeFormativo: String,
    val nota: String,
    val record: Boolean,
    val reprobado: Boolean
)