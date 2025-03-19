package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class FileForm(
    val file: List<Int>,
    val name: String
)