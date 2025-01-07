package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class FileForm(
    val file: List<Int>,
    val name: String
)