package org.example.aok.data.network.solicitud_becas

import kotlinx.serialization.Serializable

@Serializable
data class SolicitudBecaArchivo(
    val archivo: String,
    val fecha: String,
    val tipo: String
)