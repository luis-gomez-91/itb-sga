package org.example.aok.data.network.pro_cronograma

import kotlinx.serialization.Serializable

@Serializable
data class ProCronogramaProfesores(
    val desde: String,
    val hasta: String,
    val log: String,
    val profesor: String,
    val segmento: String
)