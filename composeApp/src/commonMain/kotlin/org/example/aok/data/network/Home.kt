package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Home(
    val grupoModulos: List<GrupoModulo>,
    val periodos: List<Periodo>,
    val persona: Persona
)