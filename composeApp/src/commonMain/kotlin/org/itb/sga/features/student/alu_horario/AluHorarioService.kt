package org.itb.sga.features.student.alu_horario

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.AluHorario
import org.itb.sga.data.network.AluHorarioResult
import org.itb.sga.data.network.Error

class AluHorarioService(
    private val client: HttpClient
) {
    suspend fun fetchAluHorario(id: Int): AluHorarioResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_horarios&id=$id")

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