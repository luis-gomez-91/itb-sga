package org.example.aok.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class DjangoModel(
    val results: List<DjangoModelItem>
)