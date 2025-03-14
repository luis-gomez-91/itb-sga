package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class VerClase(
    var action: String,
    var idLeccionGrupo: Int
)