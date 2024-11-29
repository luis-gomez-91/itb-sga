package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class File(
    var file: List<Int>,
    var name: String
)