package org.itb.sga.data.network.solicitud_becas

import kotlinx.serialization.Serializable

@Serializable
data class SolicitudBeca(
    val archivos: List<SolicitudBecaArchivo>,
    val carrera: String,
    val estado: String,
    val fecha: String,
    val fechaCorreo: String,
    val fechaRespuesta: String,
    val id: Int,
    val motivo: String,
    val nivel: String,
    val puntaje: Double,
    val tipo: String
)