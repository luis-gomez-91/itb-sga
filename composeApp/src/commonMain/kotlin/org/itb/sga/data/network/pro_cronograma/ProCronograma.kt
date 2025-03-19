package org.itb.sga.data.network.pro_cronograma

import kotlinx.serialization.Serializable

@Serializable
data class ProCronograma(
    val asignatura: String,
    val carrera: String,
    val fechaDesde: String,
    val fechaHasta: String,
    val grupo: String,
    val horarios: List<ProCronogramaHorarios>,
    val nivelMalla: String,
    val profesores: List<ProCronogramaProfesores>,
    val tutor: String?
)