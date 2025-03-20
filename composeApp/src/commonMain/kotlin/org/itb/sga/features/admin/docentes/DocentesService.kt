package org.itb.sga.features.admin.docentes

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Docentes
import org.itb.sga.data.network.DocentesResult
import org.itb.sga.data.network.Error

class DocentesService(
    private val client: HttpClient
) {
    suspend fun fetchDocentes(search: String, page: Int): DocentesResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=docentes&search=$search&page=${page}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<Docentes>()
                DocentesResult.Success(data)
            } else {
                val error = response.body<Error>()
                DocentesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            DocentesResult.Failure(error)
        }
    }
}