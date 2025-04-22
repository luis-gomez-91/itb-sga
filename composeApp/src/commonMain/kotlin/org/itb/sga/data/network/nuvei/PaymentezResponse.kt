package org.itb.sga.data.network.nuvei

import kotlinx.serialization.Serializable

@Serializable
data class PaymentezResponse(
    var action: String? = null,
    var id: Int? = null,
    val card: Card?,
    val transaction: Transaction?,
    val user: User? = null
)