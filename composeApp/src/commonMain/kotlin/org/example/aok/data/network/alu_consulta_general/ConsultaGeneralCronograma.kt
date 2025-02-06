package org.example.aok.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralCronograma(
    val docente: String?,
    val fechaFin: String?,
    val fechaInicio: String?,
    val materia: String
)