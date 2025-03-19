package org.itb.sga.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralAlumno(
    val carrera: String,
    val celular: String?,
    val dicapacitado: Boolean,
    val email: String,
    val modulo: ConsultaGeneralModulo,
    val nombre: String
)