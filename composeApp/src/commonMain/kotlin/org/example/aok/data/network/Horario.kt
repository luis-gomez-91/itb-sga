package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Horario(
    val aula: String,
    val claseVirtual: Boolean,
    val dia: String,
    val turnoComienza: String,
    val turnoTermina: String
)