package org.example.aok.data.network.pro_clases

import kotlinx.serialization.Serializable

@Serializable
data class LeccionGrupo(
    var asistencias: MutableList<Asistencia>,
    val leccionGrupoContenido: String,
    val leccionGrupoId: Int,
    val leccionGrupoObservaciones: String,
    val totalLecciones: Int,
    val minimoAsistencia: Int
)