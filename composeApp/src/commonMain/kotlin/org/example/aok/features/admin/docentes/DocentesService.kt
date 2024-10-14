package org.example.aok.features.admin.docentes

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Docentes
import org.example.aok.data.network.DocentesResult
import org.example.aok.data.network.Error

class DocentesService(
    private val client: HttpClient
) {
    suspend fun fetchDocentes(search: String): DocentesResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=docentes&search=$search")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<Docentes>()
                DocentesResult.Success(data)
            } else {
                val error = response.body<Error>()
                DocentesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            DocentesResult.Failure(error)
        }
    }
}