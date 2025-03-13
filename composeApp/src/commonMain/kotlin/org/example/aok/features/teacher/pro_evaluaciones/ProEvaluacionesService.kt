package org.example.aok.features.teacher.pro_evaluaciones

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProEvaluacionesResult
import org.example.aok.data.network.pro_evaluaciones.ProEvaluaciones

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
}