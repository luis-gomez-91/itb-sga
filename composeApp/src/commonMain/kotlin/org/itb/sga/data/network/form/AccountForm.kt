package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class AccountForm(
    val action: String,
    val idSexo: Int,
    val idTipoSangre: Int,
    val idEstadoCivil: Int,
    val celular: Int,
    val convencional: Int,
    val email: String,
    val idProvincia: Int,
    val idCanton: Int,
    val idParroquia: Int,
    val idSector: Int,
    val callePrincipal: String,
    val calleSecundaria: String,
    val numeroDomicilio: Int,
    val nombrePadre: String,
    val nombreMadre: String,
    val idPersona: Int
)