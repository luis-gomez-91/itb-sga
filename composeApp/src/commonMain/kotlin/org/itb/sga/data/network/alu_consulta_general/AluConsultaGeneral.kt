package org.itb.sga.data.network.alu_consulta_general

import kotlinx.serialization.Serializable

@Serializable
data class AluConsultaGeneral(
    val alumno: ConsultaGeneralAlumno,
    val cronogramas: ConsultaGeneralCronograma?,
    val finanzas: ConsultaGeneralFinanza,
    val matricula: ConsultaGeneralMatricula?
)