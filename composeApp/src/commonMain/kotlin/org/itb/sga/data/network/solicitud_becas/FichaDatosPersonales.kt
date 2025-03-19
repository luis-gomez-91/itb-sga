package org.itb.sga.data.network.solicitud_becas

import kotlinx.serialization.Serializable
import org.itb.sga.data.network.models.Canton
import org.itb.sga.data.network.models.EstadoCivil

@Serializable
data class FichaDatosPersonales(
    var `data`: FichaDatosPersonalesData,
    val estadoCivil: List<EstadoCivil>,
    val sectorResidencia: List<SectorResidencia>,
    val ciudades: List<Canton>
)