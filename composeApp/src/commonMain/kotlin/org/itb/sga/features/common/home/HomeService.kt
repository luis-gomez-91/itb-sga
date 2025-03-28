package org.itb.sga.features.common.home

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.contentType
import org.itb.sga.core.SERVER_URL
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.Home
import org.itb.sga.data.network.HomeResult
import org.itb.sga.data.network.Report
import org.itb.sga.data.network.ReportForm
import org.itb.sga.data.network.ReportResult
import org.itb.sga.data.network.notificaciones.NotificacionDetalle

class HomeService(
    private val client: HttpClient
) {
    suspend fun fetchHome(id: Int): HomeResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=panel&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val home = response.body<Home>()
                HomeResult.Success(home)
            } else {
                val error = response.body<Error>()
                HomeResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            HomeResult.Failure(error)
        }
    }

    suspend fun fetchReport(form: ReportForm): ReportResult {
        return try {
            val response =
                client.post("${SERVER_URL}api_rest?action=runReport") {
                    contentType(ContentType.Application.Json)
                    setBody(form)
                }
            if (response.status == HttpStatusCode.OK) {
                val data = response.body<Report>()
                ReportResult.Success(data)

            } else {
                val error = response.body<Error>()
                ReportResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ReportResult.Failure(error)
        } finally {
        }
    }

    suspend fun fetchNotificacionesDetalle(id: Int, idPersona: Int): NotificacionDetalle {
        return try {
            val response = client.post("${SERVER_URL}notificaciones") {
                setBody(FormDataContent(Parameters.build {
                    append("action", "mostrar_detalle")
                    append("id", id.toString())
                    append("idPersona", idPersona.toString())
                }))
            }
            response.body<NotificacionDetalle>()
        } catch (e: Exception) {
            logInfo("error", "$e")
            throw e
        }
    }
}