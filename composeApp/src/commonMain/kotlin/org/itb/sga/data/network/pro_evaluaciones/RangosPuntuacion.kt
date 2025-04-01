package org.itb.sga.data.network.pro_evaluaciones

import kotlinx.serialization.Serializable

@Serializable
data class RangosPuntuacion (
    val ASIST_PARA_APROBAR: Int,
    val PORCIENTO_NOTA1: Int,
    val PORCIENTO_NOTA2: Int,
    val PORCIENTO_NOTA3: Int,
    val PORCIENTO_NOTA4: Int,
    val PORCIENTO_NOTA5: Int,
    val MIN_APROBACION_EXAMEN: Int
)