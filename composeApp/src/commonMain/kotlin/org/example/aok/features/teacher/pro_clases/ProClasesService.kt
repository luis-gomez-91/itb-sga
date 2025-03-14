package org.example.aok.features.teacher.pro_clases

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.Error
import org.example.aok.data.network.LeccionGrupoResult
import org.example.aok.data.network.ProClases
import org.example.aok.data.network.ProClasesResult
import org.example.aok.data.network.form.ComenzarClaseForm
import org.example.aok.data.network.form.VerClase
import org.example.aok.data.network.pro_clases.LeccionGrupo

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
}