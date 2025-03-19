package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluCronograma(
    val asignatura: String,
    val creditos: Double,
    val fin: String,
    val horarios: List<Horario>,
    val horas: Double,
    val inicio: String,
    val profesores: List<Profesor>
)