package org.example.aok.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class RequestPasswordChangeForm(
    var action: String,
    var idPersona: Int,
    var previousPassword: String,
    var newPassword: String
)