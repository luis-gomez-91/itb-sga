package org.itb.sga.data.network.pro_clases

import kotlinx.serialization.Serializable

@Serializable
data class LeccionGrupo(
    var asistencias: MutableList<Asistencia>,
    var leccionGrupoContenido: String,
    val leccionGrupoId: Int,
    var leccionGrupoObservaciones: String,
    val totalLecciones: Int,
    val minimoAsistencia: Int
)