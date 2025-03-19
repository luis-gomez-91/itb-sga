package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Docentes(
    val paging: Paging,
    val profesores: List<Docente>
)