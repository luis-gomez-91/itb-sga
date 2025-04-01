package org.itb.sga.features.teacher.pro_evaluaciones

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
import kotlinx.serialization.json.Json
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.CalificacionResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.ProEvaluacionesResult
import org.itb.sga.data.network.UpdateAsistenciaResult
import org.itb.sga.data.network.form.CalificacionForm
import org.itb.sga.data.network.pro_clases.Asistencia
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluaciones
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluacionesCalificacion

class ProEvaluacionesService(
    private val client: HttpClient
) {
    suspend fun fetchProEvaluaciones(id: Int, idPeriodo: Int, idMateria: Int? = null): ProEvaluacionesResult {
        return try {
            val url = buildString {
                append(SERVER_URL, "api_rest?action=pro_evaluaciones")
                append("&id=", id)
                append("&periodo=", idPeriodo)
                idMateria?.let { append("&materia=", it) }
            }

            val response: HttpResponse = client.get(url)

            when (response.status) {
                HttpStatusCode.OK -> {
                    val data = response.body<ProEvaluaciones>()
                    ProEvaluacionesResult.Success(data)
                }
                else -> {
                    val errorBody = response.bodyAsText()
                    val error = runCatching { Json.decodeFromString<Error>(errorBody) }
                        .getOrElse { Error("Error", "Error desconocido: $errorBody") }
                    ProEvaluacionesResult.Failure(error)
                }
            }
        } catch (e: Exception) {
            ProEvaluacionesResult.Failure(Error("Error", "Error inesperado: ${e.message}"))
        }
    }

    suspend fun updateCalificacion(client: HttpClient, form: CalificacionForm): CalificacionResult {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_evaluaciones") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status == HttpStatusCode.OK) {
                val data = response.body<ProEvaluacionesCalificacion>()
                CalificacionResult.Success(data)
            } else {
                val error = response.body<Error>()
                CalificacionResult.Failure(error)
            }
        } catch (e: Exception) {
            CalificacionResult.Failure(Error("Error", "Error inesperado: ${e.message}"))
        }
    }
}