package org.itb.sga.data.network.pro_evaluaciones

import kotlinx.serialization.Serializable

@Serializable
data class ProEvaluaciones(
    val alumnos: List<ProEvaluacionesCalificacion>? = emptyList(),
    val codigos: List<ProEvaluacionesCodigo>,
    val materias: List<ProEvaluacionesMateria>
)