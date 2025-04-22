package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class FormField(
    val action: String,
    val value: String?,
    val id: Int?
)