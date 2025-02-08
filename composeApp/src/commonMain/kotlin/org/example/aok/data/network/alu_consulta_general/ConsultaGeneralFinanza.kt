package org.example.aok.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralFinanza(
    val modulo: ConsultaGeneralModulo,
    val data: List<DataConsultaGeneralRubro>
)