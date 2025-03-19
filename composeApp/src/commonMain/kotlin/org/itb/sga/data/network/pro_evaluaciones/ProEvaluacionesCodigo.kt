package org.itb.sga.data.network.pro_evaluaciones

import kotlinx.serialization.Serializable

@Serializable
data class ProEvaluacionesCodigo(
    val idCodigoEvaluacion: Int,
    val nombre: String
)