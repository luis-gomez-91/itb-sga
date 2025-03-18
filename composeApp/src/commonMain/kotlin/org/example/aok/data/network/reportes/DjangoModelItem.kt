package org.example.aok.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class DjangoModelItem (
    val id: Int,
    val name: String
)