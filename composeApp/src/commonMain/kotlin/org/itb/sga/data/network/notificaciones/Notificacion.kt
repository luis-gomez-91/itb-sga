package org.itb.sga.data.network.notificaciones

import kotlinx.serialization.Serializable

@Serializable
data class Notificacion(
    val id: Int,
    val notificacion_descripcion: String,
    val notificacion_titulo: String,
    val tipo: String,
    val urls: List<NotificacionUrl>? = null,
    val utiliza_detalle: Boolean,
    var detail: NotificacionDetalle? = null
)