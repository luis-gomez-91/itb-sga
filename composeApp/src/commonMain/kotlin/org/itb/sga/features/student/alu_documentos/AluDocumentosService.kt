package org.itb.sga.features.student.alu_documentos

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.AluDocumento
import org.itb.sga.data.network.AluDocumentosResult
import org.itb.sga.data.network.Error

class AluDocumentosService(
    private val client: HttpClient
) {
    suspend fun fetchAluDocumentos(id: Int): AluDocumentosResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=documentos_alu&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluDocumento>>()
                AluDocumentosResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluDocumentosResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluDocumentosResult.Failure(error)
        }
    }
}