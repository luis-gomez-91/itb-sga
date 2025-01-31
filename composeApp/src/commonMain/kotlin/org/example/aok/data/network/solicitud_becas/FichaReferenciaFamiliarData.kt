package org.example.aok.data.network.solicitud_becas

import kotlinx.serialization.Serializable
import org.example.aok.data.network.models.Parentesco

@Serializable
data class FichaReferenciaFamiliarData(
    val id: Int?,
    val parentesco: Parentesco?,
    val telefono: String
)