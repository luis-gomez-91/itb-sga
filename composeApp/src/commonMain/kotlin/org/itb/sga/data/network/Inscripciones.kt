package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Inscripciones(
    val inscripciones: List<Inscripcion>,
    val paging: Paging
)