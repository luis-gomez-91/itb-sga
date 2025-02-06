package org.example.aok.data.network.alu_consulta_general

import kotlinx.serialization.Serializable

@Serializable
data class AluConsultaGeneral(
    val alumno: ConsultaGeneralAlumno,
    val cronograma: List<ConsultaGeneralCronograma>?,
    val finanzas: List<ConsultaGeneralFinanza>?,
    val matricula: ConsultaGeneralMatricula?
)