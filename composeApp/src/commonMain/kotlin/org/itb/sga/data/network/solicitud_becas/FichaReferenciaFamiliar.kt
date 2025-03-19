package org.itb.sga.data.network.solicitud_becas

import kotlinx.serialization.Serializable
import org.itb.sga.data.network.models.Parentesco

@Serializable
data class FichaReferenciaFamiliar(
    val `data`: List<FichaReferenciaFamiliarData>?,
    val parentesco: List<Parentesco>,
)