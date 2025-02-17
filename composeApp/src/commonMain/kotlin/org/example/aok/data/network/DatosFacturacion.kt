package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class DatosFacturacion(
    var cedula: String,
    var correo: String,
    var nombre: String,
    var telefono: String,
    var direccion: String = ""
)