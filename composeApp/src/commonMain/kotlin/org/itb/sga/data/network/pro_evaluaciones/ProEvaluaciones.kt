package org.itb.sga.data.network.pro_evaluaciones

import kotlinx.serialization.Serializable

@Serializable
data class ProEvaluaciones(
    var alumnos: MutableList<ProEvaluacionesCalificacion>? = null,
    val codigos: List<ProEvaluacionesCodigo>,
    val materias: List<ProEvaluacionesMateria>
)