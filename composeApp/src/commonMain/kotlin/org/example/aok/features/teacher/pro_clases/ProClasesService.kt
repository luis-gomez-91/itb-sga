package org.example.aok.features.teacher.pro_clases

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProClases
import org.example.aok.data.network.ProClasesResult

class ProClasesService(
    private val client: HttpClient
) {
    suspend fun fetchProClases(search: String, page: Int, id: Int): ProClasesResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=pro_clases&id=${id}&page=${page}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<ProClases>()
                ProClasesResult.Success(data)
            } else {
                val error = response.body<Error>()
                ProClasesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            ProClasesResult.Failure(error)
        }
    }
}