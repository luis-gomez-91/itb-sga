package org.example.aok.features.common.reportes

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.DjangoModelResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.ReportesResult
import org.example.aok.data.network.reportes.DjangoModel
import org.example.aok.data.network.reportes.ReporteCategoria

class ReportesService(
    private val client: HttpClient
) {
    suspend fun fetchReportes(idPersona: Int): ReportesResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=reportes&id=${idPersona}")

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

//    suspend fun fetchDjangoModel(model: String): DjangoModelResult {
//        return try {
//            val response = client.get("${SERVER_URL}api_rest?action=reportes&model=${model}")
//
//            if (response.status == HttpStatusCode.OK) {
//                val data = response.body<List<DjangoModel>>()
//                DjangoModelResult.Success(data)
//            } else {
//                val error = response.body<Error>()
//                DjangoModelResult.Failure(error)
//            }
//        } catch (e: Exception) {
//            val error = Error("Error", "Error inesperado: ${e.message}")
//            DjangoModelResult.Failure(error)
//        }
//    }

    suspend fun fetchDjangoModel(model: String, query:String): DjangoModelResult {
        return try {
            val response = client.get("${SERVER_URL}reportes?action=data&model=${model}&p=1&q=${query}&s=10")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<DjangoModel>()
                DjangoModelResult.Success(data)
            } else {
                val error = response.body<Error>()
                DjangoModelResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            DjangoModelResult.Failure(error)
        }
    }
}