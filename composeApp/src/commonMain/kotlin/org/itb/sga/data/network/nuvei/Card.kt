package org.itb.sga.data.network.nuvei

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val bin: String?,
    val holder_name: String? = null,
    val number: String?,
    val origin: String?,
    val type: String?
)