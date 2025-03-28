package org.itb.sga.data.network.notificaciones

import kotlinx.serialization.Serializable

@Serializable
data class NotificacionDetalleItem(
    val detalle_tabla: List<String>
)