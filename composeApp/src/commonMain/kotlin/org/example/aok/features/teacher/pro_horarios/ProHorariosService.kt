package org.example.aok.features.teacher.pro_horarios

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.ComenzarClaseResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorariosResult
import org.example.aok.data.network.form.ComenzarClaseForm
import org.example.aok.data.network.pro_clases.ComenzarClase

class ProHorariosService(
    private val client: HttpClient
) {
    suspend fun fetchProHorarios(periodo: Periodo, id: Int): ProHorariosResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=pro_horarios&id=${id}&periodo=${periodo.id}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<ProHorario>>()
                ProHorariosResult.Success(data)
            } else {
                val error = response.body<Error>()
                ProHorariosResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ProHorariosResult.Failure(error)
        }
    }

    suspend fun comenzarClase(client: HttpClient, form: ComenzarClaseForm): ComenzarClaseResult {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_horarios") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status == HttpStatusCode.OK) {
                val data = response.body<ComenzarClase>()
                ComenzarClaseResult.Success(data)
            } else {
                val error = response.body<Error>()
                ComenzarClaseResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ComenzarClaseResult.Failure(error)
        }
    }
}