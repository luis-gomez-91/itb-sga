package org.itb.sga.data.network.pro_entrega_actas

import kotlinx.serialization.Serializable

@Serializable
data class ProEntregaActas(
    val asignatura: String,
    val carrera: String,
    val desde: String,
    val docentes: List<ProEntregaActasDocente>,
    val estado: String,
    val grupo: String,
    val hasta: String,
    val idMateria: Int,
    val nivelCerrado: Boolean,
    val nivelMalla: String
)