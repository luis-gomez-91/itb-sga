package org.itb.sga.features.student.alu_solicitudes_online

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.itb.sga.core.SERVER_URL
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.AluSolicitud
import org.itb.sga.data.network.AluSolicitudes
import org.itb.sga.data.network.AluSolicitudesResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.SolicitudEspecieForm

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

    suspend fun  fetchTipoEspecies(idInscripcion: Int): AluSolicitudes {
        val response = client.get("${SERVER_URL}api_rest?action=solicitudonline&a=fetch_tipo_especies&idInscripcion=${idInscripcion}")
        logInfo("alu_solicitudes", response.body())
        return response.body()
    }

    suspend fun addSolicitud(form: SolicitudEspecieForm): Response {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=solicitudonline") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status.isSuccess()) {
                val successResponse = response.body<Response>()
                successResponse
            } else {
                Response(status = "error", message = "Unexpected response status: ${response.status}")
            }
        } catch (e: Exception) {
            logInfo("prueba", "ERROR: ${e.message}")
            e.printStackTrace()
            Response(status = "error", message = "Exception occurred: ${e.message}")
        }
    }
}