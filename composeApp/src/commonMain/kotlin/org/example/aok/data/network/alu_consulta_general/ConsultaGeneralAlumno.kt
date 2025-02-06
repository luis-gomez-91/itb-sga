package org.example.aok.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralAlumno(
    val carrera: String,
    val celular: String?,
    val dicapacitado: Boolean,
    val email: String,
    val modulos: List<ConsultaGeneralModulo>,
    val nombre: String
)