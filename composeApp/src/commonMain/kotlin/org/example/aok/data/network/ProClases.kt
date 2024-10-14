package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class ProClases(
    val clases: List<ClaseX>,
    val paging: Paging
)