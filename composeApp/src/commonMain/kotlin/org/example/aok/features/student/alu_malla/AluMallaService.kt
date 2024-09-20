package org.example.aok.features.student.alu_malla

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.AluFinanzasResult
import org.example.aok.data.network.AluMalla
import org.example.aok.data.network.AluMallaResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.Rubro

class AluMallaService(
    private val client: HttpClient
) {
    suspend fun fetchAluMalla(id: Int): AluMallaResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=alu_malla&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluMalla>>()
                AluMallaResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluMallaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            AluMallaResult.Failure(error)
        }
    }
}