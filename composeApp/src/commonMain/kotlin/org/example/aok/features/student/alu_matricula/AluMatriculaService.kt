package org.example.aok.features.student.alu_matricula

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.AluMatriculaResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.alu_consulta_general.AluConsultaGeneral

class AluMatriculaService(
    private val client: HttpClient
) {
    suspend fun fetchAluMatricula(id: Int): AluMatriculaResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_matricula&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<AluConsultaGeneral>()
                AluMatriculaResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluMatriculaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluMatriculaResult.Failure(error)
        }
    }
}