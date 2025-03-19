package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    var idPersona: Int,
    var nombre: String,
    var photo: String,
    var idUsuario: Int
)