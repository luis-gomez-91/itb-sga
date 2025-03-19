package org.itb.sga.features.student.alu_notas

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.AluNotasResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.TipoAsignaturaNota

class AluNotasService(
    private val client: HttpClient
) {
    suspend fun fetchAluNota(id: Int): AluNotasResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_notas&id=$id")
            println(response.bodyAsText())

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<TipoAsignaturaNota>()
                AluNotasResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluNotasResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluNotasResult.Failure(error)
        }
    }
}