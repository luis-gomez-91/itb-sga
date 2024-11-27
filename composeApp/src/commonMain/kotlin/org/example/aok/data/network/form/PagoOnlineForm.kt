package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class PagoOnlineForm(
    val action: String = "facturarPagoOnline",
    val inscripcion: Int,
    val valor: Double,
//    val rubros: List<Int>,
    val correo: String,
//    val direccion: String,
    val nombre: String,
    val ruc: String,
    val telefono: String,
//    val fechaTransaccion: String,
)