package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluMalla(
    val asignaturas: List<AluMallaAsignatura>,
    val nivelmallaNombre: String,
    val nivelmallaNombreCorto: String?
)