package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class SolicitudEspecieForm(
    var action: String,
    var idEspecie: Int,
    var observacion: String,
    var idInscripcion: Int,
    var file: FileForm?,
    var idAsignatura: Int?,
    var idDocente: Int?
)