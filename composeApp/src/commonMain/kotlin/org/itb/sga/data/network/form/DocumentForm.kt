package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class DocumentForm(
    val action: String,
    val idDocumento: Int,
    val file: List<Int>?,
    val fileName: String?,
    val idPersona: Int
)

