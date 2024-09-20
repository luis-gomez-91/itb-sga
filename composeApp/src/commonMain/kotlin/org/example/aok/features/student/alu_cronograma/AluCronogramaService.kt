package org.example.aok.features.student.alu_cronograma


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluCronograma
import org.example.aok.data.network.AluCronogramaResult
import org.example.aok.data.network.Error

class AluCronogramaService(
    private val client: HttpClient
) {
    suspend fun fetchAluCronograma(id: Int): AluCronogramaResult {
        return try {
            logInfo("alu_cronograma", id.toString())
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=alu_cronograma&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluCronograma>>()
                AluCronogramaResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluCronogramaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            AluCronogramaResult.Failure(error)
        }
    }
}

