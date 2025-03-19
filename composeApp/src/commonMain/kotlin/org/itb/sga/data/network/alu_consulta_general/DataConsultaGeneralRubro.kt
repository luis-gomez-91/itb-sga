package org.itb.sga.data.network.alu_consulta_general

import kotlinx.serialization.Serializable

@Serializable
data class DataConsultaGeneralRubro(
    val abono: Int,
    val fechaVence: String,
    val nombre: String,
    val saldo: Double,
    val valor: Double
)