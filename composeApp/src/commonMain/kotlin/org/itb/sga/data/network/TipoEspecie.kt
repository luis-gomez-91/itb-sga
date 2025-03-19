package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class TipoEspecie(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val usaArchivo: Boolean,
    val relacionaDocente: Boolean
)