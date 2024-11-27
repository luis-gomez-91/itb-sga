package org.example.aok.features.student.alu_solicitudes_online

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.json.Json
import org.example.aok.core.SERVER_URL
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluSolicitud
import org.example.aok.data.network.AluSolicitudDepartamentos
import org.example.aok.data.network.AluSolicitudesResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.form.SolicitudEspecieForm

class AluSolicitudesService(
    private val client: HttpClient
) {
    suspend fun fetchAluSolicitudes(id: Int): AluSolicitudesResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=solicitudonline&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluSolicitud>>()
                AluSolicitudesResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluSolicitudesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluSolicitudesResult.Failure(error)
        }
    }

    suspend fun  fetchTipoEspecies(): List<AluSolicitudDepartamentos> {
        val response = client.get("http://10.0.2.2:8000/api_rest?action=solicitudonline&a=fetch_tipo_especies")
        logInfo("alu_solicitudes", response.body())
        return response.body()
    }

    suspend fun addSolicitud(form: SolicitudEspecieForm): HttpResponse {
        return client.post("http://10.0.2.2:8000/api_rest?action=solicitudonline") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }
    }
}