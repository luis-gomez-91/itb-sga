package org.itb.sga.data.network.pro_cronograma

import kotlinx.serialization.Serializable

@Serializable
data class ProCronogramaHorarios(
    val aula: String,
    val dia: String,
    val horario: String
)