package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class EntregarActaForm(
    val action: String,
    val idMateria: Int,
    val fileActa: List<Int>?,
    val fileInforme: List<Int>?,
    val nameActa: String?,
    val nameInforme: String?,
    val idProfesor: Int,
    val observaciones: String
)