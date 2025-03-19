package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluSolicitudes(
    var departamentos: List<AluSolicitudDepartamentos> = emptyList(),
    val asignaturas: List<TipoEspecieAsignatura>?
)