package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluFacturacion(
    val fecha: String,
    val numero: String,
    val numeroAutorizacion: String,
    val ride: String,
    val tipo: String,
    val xml: String
)

data class AluFacturacionXML(
    val result: String,
    val reportfile: String
)

data class AluFacturacionRIDE(
    val result: String,
    val reportfile: String
)