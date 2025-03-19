package org.itb.sga.data.network.alu_consulta_general

import kotlinx.serialization.Serializable

@Serializable
data class DataConsultaGeneralMateria(
    val docente: String?,
    val fechaFin: String?,
    val fechaInicio: String?,
    val materia: String
)