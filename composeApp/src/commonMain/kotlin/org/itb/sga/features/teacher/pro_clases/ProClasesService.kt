package org.itb.sga.features.teacher.pro_clases

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.LeccionGrupoResult
import org.itb.sga.data.network.ProClases
import org.itb.sga.data.network.ProClasesResult
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.UpdateAsistenciaResult
import org.itb.sga.data.network.form.UpdateAsistencia
import org.itb.sga.data.network.form.UpdateValue
import org.itb.sga.data.network.form.VerClase
import org.itb.sga.data.network.pro_clases.Asistencia
import org.itb.sga.data.network.pro_clases.LeccionGrupo

class ProClasesService(
    private val client: HttpClient
) {
    suspend fun fetchProClases(search: String, page: Int, id: Int): ProClasesResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=pro_clases&id=${id}&page=${page}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<ProClases>()
                ProClasesResult.Success(data)
            } else {
                val error = response.body<Error>()
                ProClasesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ProClasesResult.Failure(error)
        }
    }

    suspend fun verClase(client: HttpClient, form: VerClase): LeccionGrupoResult {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_clases") {
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

    suspend fun updateAsistencia(client: HttpClient, form: UpdateAsistencia): UpdateAsistenciaResult {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_clases") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status == HttpStatusCode.OK) {
                val data = response.body<Asistencia>()
                UpdateAsistenciaResult.Success(data)
            } else {
                val error = response.body<Error>()
                UpdateAsistenciaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            UpdateAsistenciaResult.Failure(error)
        }
    }

    suspend fun updateContenido(client: HttpClient, form: UpdateValue): Response {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_clases") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<Response>()
            } else {
                response.body<Response>()
            }
        } catch (e: Exception) {
            Response("Error", "Error inesperado: ${e.message}")
        }
    }
}