package org.example.aok.data.network.solicitud_becas

import kotlinx.serialization.Serializable
import org.example.aok.data.network.models.Parentesco

@Serializable
data class FichaReferenciaFamiliar(
    val `data`: List<FichaReferenciaFamiliarData>?,
    val parentesco: List<Parentesco>,
)