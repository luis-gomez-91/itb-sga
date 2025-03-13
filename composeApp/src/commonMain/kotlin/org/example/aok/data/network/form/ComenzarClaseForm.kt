package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class ComenzarClaseForm(
    var action: String,
    var idClase: Int,
    var idProfesor: Int
)