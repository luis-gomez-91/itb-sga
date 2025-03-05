package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Docente(
    val activo: Boolean,
    val categoria: String,
    val celular: String,
    val convencional: String,
    val dedicacion: String,
    val email: String,
    val email_personal: String,
    val fechaIngreso: String,
    val foto: String,
    val id: Int,
    val idPersona: Int,
    val idUsuario: Int,
    val identificacion: String,
    val nombre: String,
    val password: String,
    val username: String
)