package org.example.aok.features.common.reportes

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.Error
import org.example.aok.data.network.ReportesResult
import org.example.aok.data.network.reportes.ReporteCategoria

class ReportesService(
    private val client: HttpClient
) {
    suspend fun fetchReportes(id: Int): ReportesResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=reportes&id=${id}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<ReporteCategoria>>()
                ReportesResult.Success(data)
            } else {
                val error = response.body<Error>()
                ReportesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ReportesResult.Failure(error)
        }
    }
}