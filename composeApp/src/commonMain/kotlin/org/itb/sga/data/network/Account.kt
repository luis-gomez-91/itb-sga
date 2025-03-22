package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val cantonNacimiento: String,
    val celular: String? = null,
    var convencional: String? = null,
    var domicilioCallePrincipal: String? = null,
    var domicilioCalleSecundaria: String? = null,
    var domicilio_numero: String? = null,
    var email: String? = null,
    val emailinst: String,
    val extranjero: Boolean,
    var fechaNacimiento: String? = null,
    val foto: String,
    val idCantonResidencia: Int? = null,
    var idParroquia: Int? = null,
    var idPersona: Int? = null,
    val idProvinciaResidencia: Int? = null,
    var idTipoSangre: Int? = null,
    val identificacion: String,
    var madre: String? = null,
    var nacionalidad: String? = null,
    val nombre: String,
    val nombreCantonResidencia: String? = null,
    val nombreParroquia: String?,
    val nombreProvinciaResidencia: String? = null,
    var nombreTipoSangre: String? = null,
    var padre: String? = null,
    val sector: String? = null,
    var provinciaNacimiento: String? = null,
    val sexo: String? = null,
    val username: String,
    val tipoIdentificacion: String
)
