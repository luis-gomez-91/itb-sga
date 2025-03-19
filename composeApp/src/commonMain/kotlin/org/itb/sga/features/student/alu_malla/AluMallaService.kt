package org.itb.sga.features.student.alu_malla

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.AluMalla
import org.itb.sga.data.network.AluMallaResult
import org.itb.sga.data.network.Error

class AluMallaService(
    private val client: HttpClient
) {
    suspend fun fetchAluMalla(id: Int): AluMallaResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_malla&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluMalla>>()
                AluMallaResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluMallaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluMallaResult.Failure(error)
        }
    }
}