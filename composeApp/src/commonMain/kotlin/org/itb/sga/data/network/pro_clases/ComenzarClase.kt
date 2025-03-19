package org.itb.sga.data.network.pro_clases

import kotlinx.serialization.Serializable
import org.itb.sga.data.network.ClaseX

@Serializable
data class ComenzarClase(
    val leccionGrupo: LeccionGrupo,
    val claseX: ClaseX
)