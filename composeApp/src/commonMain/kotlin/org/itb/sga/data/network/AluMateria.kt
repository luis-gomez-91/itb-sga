package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluMateria(
    val asistencias: String,
    val estado: String,
    val materiaFin: String,
    val materiaInicio: String,
    val materiaNombre: String,
    val numAsistencias: Int,
    val numLecciones: Int,
    val profesores: List<AluMateriaProfesor>?,
    val lecciones: List<AluMateriaLeccion>?,
    val calificaciones:AluMateriaCalificacion?,
    val notaFinal: Double
)