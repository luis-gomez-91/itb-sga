package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class TipoAsignaturaNota(
    val asignaturas: List<AluNotas>,
    val otrasnotas: List<OtraNotaAsignatura>
)
