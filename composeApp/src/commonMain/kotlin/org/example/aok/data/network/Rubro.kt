package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Rubro (
    val cancelado: Boolean,
    val fecha: String,
    val fechaVencimiento: String,
    val pagado: String,
    val porPagar: String,
    val rubro: String,
    val valor: String,
    val vencido: String
)