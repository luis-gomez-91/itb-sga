package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Persona(
    val celular: String,
    val convencional: String,
    val email: String,
    val emailinst: String,
    val esInscripcion: Boolean,
    val esDocente: Boolean,
    val foto: String,
    val idInscripcion: Int?,
    val idDocente: Int?,
    val idPersona: Int,
    val identificacion: String,
    val nacionalidadEmoticon: String,
    val nombre: String,
    val sexo: String,
    val tipoIdentificacion: String,
    val usuario: String
)