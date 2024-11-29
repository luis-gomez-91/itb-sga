package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class TipoEspecieAsignatura(
    val id: Int,
    val nombre: String,
    val docentes: List<TipoEspecieDocente>?
)