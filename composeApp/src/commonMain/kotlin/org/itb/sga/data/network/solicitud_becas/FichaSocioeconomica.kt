package org.itb.sga.data.network.solicitud_becas

import kotlinx.serialization.Serializable

@Serializable
data class FichaSocioeconomica(
    val datosPersonales: FichaDatosPersonales,
    val referenciaFamiliar: FichaReferenciaFamiliar
)