package org.itb.sga.data.network.reportes

import kotlinx.serialization.Serializable

@Serializable
data class DjangoModelItem (
    val id: Int,
    val name: String
)