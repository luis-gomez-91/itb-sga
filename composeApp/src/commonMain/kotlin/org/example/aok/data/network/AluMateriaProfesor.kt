package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class AluMateriaProfesor(
    val numClases: Int,
    val profesorNombre: String
)