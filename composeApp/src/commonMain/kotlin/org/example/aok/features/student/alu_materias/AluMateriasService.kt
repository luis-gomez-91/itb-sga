package org.example.aok.features.student.alu_materias

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.AluMateria
import org.example.aok.data.network.AluMateriasResult
import org.example.aok.data.network.Error

class AluMateriasService(
    private val client: HttpClient
) {
    suspend fun fetchAluMaterias(id: Int): AluMateriasResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_materias&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluMateria>>()
                AluMateriasResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluMateriasResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluMateriasResult.Failure(error)
        }
    }
}