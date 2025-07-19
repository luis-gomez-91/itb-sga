package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class AppUpdateForm(
    val platform: String
)
