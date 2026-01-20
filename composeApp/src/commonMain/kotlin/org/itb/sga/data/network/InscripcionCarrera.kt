package org.itb.sga.data.network
import kotlinx.serialization.Serializable


@Serializable
class InscripcionCarrera (
    val id: Int, //id de la inscripcion
    val carrera: String // nombre de la carrera
)