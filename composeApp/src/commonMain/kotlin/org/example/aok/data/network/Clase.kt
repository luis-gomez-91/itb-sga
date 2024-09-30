package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Clase(
    val aula: String,
    val dia: String,
    val esPractica: Boolean,
    val profesor: String,
    val sede: String,
    val sesion: String,
    val turnoComienza: String,
    val turnoHoras: String,
    val turnoTermina: String,
    val virtual: Boolean
)