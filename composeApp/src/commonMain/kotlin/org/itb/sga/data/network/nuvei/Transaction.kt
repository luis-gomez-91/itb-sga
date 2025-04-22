package org.itb.sga.data.network.nuvei

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val amount: Double?,
    val application_code: String? = null,
    val authorization_code: String?,
    val bank_name: String? = null,
    val carrier_code: String?,
    val current_status: String?,
    val date: String? = null,
    val dev_reference: String?,
    val id: String?,
    val installments: Int?,
    val installments_type: String?,
    val lot_number: String?,
    val message: String?,
    val order_description: String? = null,
    val paid_date: String? = null,
    val payment_method_type: String?,
    val status: String?,
    val status_detail: Int?,
    val stoken: String? = null,
    val terminal_code: String? = null,
    val trace_number: String?
)