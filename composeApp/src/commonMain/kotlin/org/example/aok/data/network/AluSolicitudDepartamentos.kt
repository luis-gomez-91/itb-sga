package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluSolicitudDepartamentos(
    var especies: List<TipoEspecie> = emptyList(),
    val id: Int,
    val nombre: String
)