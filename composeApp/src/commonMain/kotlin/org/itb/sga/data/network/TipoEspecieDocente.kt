package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class TipoEspecieDocente(
    val id: Int,
    val nombre: String
)