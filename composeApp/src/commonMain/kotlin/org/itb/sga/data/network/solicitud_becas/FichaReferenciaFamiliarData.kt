package org.itb.sga.data.network.solicitud_becas

import kotlinx.serialization.Serializable
import org.itb.sga.data.network.models.Parentesco

@Serializable
data class FichaReferenciaFamiliarData(
    val id: Int?,
    val parentesco: Parentesco?,
    val telefono: String
)