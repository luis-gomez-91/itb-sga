package org.example.aok.data.network.pro_clases

import kotlinx.serialization.Serializable
import org.example.aok.data.network.ClaseX

@Serializable
data class ComenzarClase(
    val leccionGrupo: LeccionGrupo,
    val claseX: ClaseX
)