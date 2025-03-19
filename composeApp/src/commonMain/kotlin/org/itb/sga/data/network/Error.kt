package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    var title: String = "Ocurrio un error",
    val error: String
)
