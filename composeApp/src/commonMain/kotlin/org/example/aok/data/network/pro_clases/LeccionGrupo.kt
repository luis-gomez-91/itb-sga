package org.example.aok.data.network.pro_clases

import kotlinx.serialization.Serializable

@Serializable
data class LeccionGrupo(
    val asistencias: List<Asistencia>,
    val leccionGrupoContenido: String,
    val leccionGrupoId: Int,
    val leccionGrupoObservaciones: String
)