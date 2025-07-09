package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class PagoOnlineForm(
    val action: String,
    val inscripcion: Int,
    val rubros: List<Int>,
    val correo: String?,
    val valor: Double,
    val direccion: String,
    val nombre: String,
    val ruc: String,
    val telefono: String?,
    val diferido: Int
)