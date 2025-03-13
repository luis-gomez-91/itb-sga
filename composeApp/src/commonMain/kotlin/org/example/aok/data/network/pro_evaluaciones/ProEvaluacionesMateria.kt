package org.example.aok.data.network.pro_evaluaciones

import kotlinx.serialization.Serializable

@Serializable
data class ProEvaluacionesMateria(
    val cerrado: Boolean,
    val desde: String,
    val fechaCierre: String?,
    val grupo: String,
    val hasta: String,
    val idMateria: Int,
    val nivelMalla: String,
    val nombre: String
)