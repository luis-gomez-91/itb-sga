package org.example.aok.data.network.form

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.aok.data.network.TipoEspecieAsignatura
import org.example.aok.data.network.TipoEspecieDocente

@Serializable
data class SolicitudEspecieForm(
    var action: String,
    var idEspecie: Int,
    var observacion: String,
    var idInscripcion: Int,
    var file: File?,
    var idAsignatura: Int?,
    var idDocente: Int?
)