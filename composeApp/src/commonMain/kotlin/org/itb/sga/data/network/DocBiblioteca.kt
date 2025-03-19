package org.itb.sga.data.network

import kotlinx.serialization.Serializable

@Serializable
data class DocBiblioteca(
    val anno: Int,
    val autor: String?,
    val codigo: String,
    val codigodewey: String,
    val emision: String,
    val nombre: String,
    val tipo: String,
    val url: String
)