package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluMateriaCalificacion(
    val n1: Int,
    val n2: Int,
    val n3: Int,
    val n4: Int,
    val examen: Int,
    val recuperacion: Int,
    val parcial: Double?,
    val total: Double?,
    val estado: String,
)