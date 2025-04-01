package org.itb.sga.data.network.form

import kotlinx.serialization.Serializable

@Serializable
data class CalificacionForm(
    val action: String,
    val nota: String,
    val valor: Int,
    val idMateriaAsignada: Int
)