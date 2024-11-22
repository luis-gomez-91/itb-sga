package org.example.aok.data.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Serializable
data class AluSolicitud (
    val aplicada: Boolean,
    val autorizado: Boolean,
    val departamento: String,
    val fecha: String,
    val numeroSerie: Int,
    val observacion: String?,
//    val pagado: JsonElement,
    val resolucion: String?,
    val respuesta: String?,
    val respuestaDocente: String?,
    val tipoEspecie: String?,
    val usuarioAsignado: String?,
    val valor: Double?
)