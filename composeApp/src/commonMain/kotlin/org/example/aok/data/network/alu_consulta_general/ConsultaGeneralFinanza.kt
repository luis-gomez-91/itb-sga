package org.example.aok.data.network.alu_consulta_general
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaGeneralFinanza(
    val abono: Int,
    val fechaVence: String,
    val nombre: String,
    val saldo: Double,
    val valor: Double
)