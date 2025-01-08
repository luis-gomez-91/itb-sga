package org.example.aok.data.network

import kotlinx.serialization.Serializable

@Serializable
data class DocBibliotecas(
    val documentos: List<DocBiblioteca>,
    val paging: Paging
)