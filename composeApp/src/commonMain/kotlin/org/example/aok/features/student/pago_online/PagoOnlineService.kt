package org.example.aok.features.student.pago_online

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.example.aok.data.network.Error
import org.example.aok.data.network.Home
import org.example.aok.data.network.HomeResult
import org.example.aok.data.network.PagoOnline
import org.example.aok.data.network.PagoOnlineForm
import org.example.aok.data.network.PagoOnlineResult

class PagoOnlineRepository(
    private val client: HttpClient
) {
    suspend fun sendPagoOnline(pagoOnlieForm: PagoOnlineForm): HttpResponse {
        return client.post("https://sga.itb.edu.ec/api_rest?action=online/") {
            contentType(ContentType.Application.Json)
            setBody(pagoOnlieForm)
        }
    }
}

class PagoOnlineService(
    private val client: HttpClient
) {
    suspend fun fetchPagoOnline(id: Int): PagoOnlineResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=online&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val pagoOnline = response.body<PagoOnline>()
                PagoOnlineResult.Success(pagoOnline)
            } else {
                val error = response.body<Error>()
                PagoOnlineResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            PagoOnlineResult.Failure(error)
        }
    }

    suspend fun sendPagoOnline(pagoOnlieForm: PagoOnlineForm): HttpResponse {
        return client.post("https://sga.itb.edu.ec/api_rest?action=facturarPagoOnline") {
            contentType(ContentType.Application.Json)
            setBody(pagoOnlieForm)
        }
    }
}