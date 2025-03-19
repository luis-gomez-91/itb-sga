package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Notificacion(
    val id: Int,
    val notificacion_descripcion: String,
    val notificacion_titulo: String,
    val tipo: String,
    val urls: List<NotificacionUrl>,
    val utiliza_detalle: Boolean
)