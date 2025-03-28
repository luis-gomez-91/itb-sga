package org.itb.sga.data.network.notificaciones

import kotlinx.serialization.Serializable

@Serializable
data class NotificacionDetalle(
    val cabecera_tabla: List<String>,
    val detalle: List<NotificacionDetalleItem>
)