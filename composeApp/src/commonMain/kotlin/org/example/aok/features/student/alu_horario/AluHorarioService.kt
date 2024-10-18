package org.example.aok.features.student.alu_horario

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.AluHorario
import org.example.aok.data.network.AluHorarioResult
import org.example.aok.data.network.Error

class AluHorarioService(
    private val client: HttpClient
) {
    suspend fun fetchAluHorario(id: Int): AluHorarioResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=alu_horarios&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluHorario>>()
                AluHorarioResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluHorarioResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluHorarioResult.Failure(error)
        }
    }
}