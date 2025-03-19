package org.itb.sga.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class EstadoCivil(
    val descripcion: String,
    val id: Int
)