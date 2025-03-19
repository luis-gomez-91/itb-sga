package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class UploadPhotoForm(
    var action: String,
    var idPersona: Int,
    var file: List<Int>,
)