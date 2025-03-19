package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class TipoEspecieAsignatura(
    val id: Int,
    val nombre: String,
    val docentes: List<TipoEspecieDocente>?
)