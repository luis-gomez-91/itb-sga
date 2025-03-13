package org.example.aok.features.teacher.pro_horarios

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.aok.core.SERVER_URL
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.LeccionGrupoResult
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorariosResult
import org.example.aok.data.network.Response
import org.example.aok.data.network.form.ComenzarClaseForm
import org.example.aok.data.network.pro_clases.LeccionGrupo

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

    suspend fun comenzarClase(client: HttpClient, form: ComenzarClaseForm): LeccionGrupoResult {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_horarios") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status == HttpStatusCode.OK) {
                val data = response.body<LeccionGrupo>()
                LeccionGrupoResult.Success(data)
            } else {
                val error = response.body<Error>()
                LeccionGrupoResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            LeccionGrupoResult.Failure(error)
        }
    }
}