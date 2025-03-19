package org.itb.sga.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralMatricula(
    val grupo: String,
    val horaFin: String,
    val horaInicio: String,
    val nivel: String,
    val sesion: String,
    val modulo: ConsultaGeneralModulo
)