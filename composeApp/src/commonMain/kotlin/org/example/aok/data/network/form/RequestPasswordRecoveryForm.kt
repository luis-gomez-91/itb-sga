package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class RequestPasswordRecoveryForm(
    var action: String,
    var username: String,
    var phone: String,
    var password: String?
)