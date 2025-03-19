package org.itb.sga.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralModulo(
    val descripcion: String,
    val url: String
)