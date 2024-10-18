package org.example.aok.features.student.alu_finanzas

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Rubro
import org.example.aok.data.network.AluFinanzasResult
import org.example.aok.data.network.Error

class AluFinanzasService(
    private val client: HttpClient
) {
    suspend fun fetchAluFinanzas(id: Int): AluFinanzasResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=alu_finanzas&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<Rubro>>()
                AluFinanzasResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluFinanzasResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluFinanzasResult.Failure(error)
        }
    }
}