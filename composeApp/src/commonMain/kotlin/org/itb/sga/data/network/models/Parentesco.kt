package org.itb.sga.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class Parentesco(
    val nombre: String,
    val id: Int
)