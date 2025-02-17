package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    var idPersona: Int,
    var nombre: String,
    var photo: String,
    var idUsuario: Int
)