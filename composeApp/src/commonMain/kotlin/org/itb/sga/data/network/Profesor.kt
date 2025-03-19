package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Profesor(
    val auxiliar: Boolean,
    val desde: String,
    val hasta: String,
    val idZoom: String? = null,
    val profesor: String,
    val segmento: String
)