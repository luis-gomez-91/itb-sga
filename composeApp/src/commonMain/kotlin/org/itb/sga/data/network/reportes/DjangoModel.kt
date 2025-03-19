package org.itb.sga.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class DjangoModel(
    val results: List<DjangoModelItem>
)