package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAsistencia (
    val action: String,
    val idAsistencia: Int,
    val value: Boolean
)
