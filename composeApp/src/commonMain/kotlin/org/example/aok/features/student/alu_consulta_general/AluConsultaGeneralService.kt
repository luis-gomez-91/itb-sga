package org.example.aok.features.student.alu_consulta_general

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.AluConsultaGeneralResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.alu_consulta_general.AluConsultaGeneral

class AluConsultaGeneralService(
    private val client: HttpClient
) {
    suspend fun fetchAluConsultaGeneral(id: Int): AluConsultaGeneralResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=consultaalumno&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<AluConsultaGeneral>()
                AluConsultaGeneralResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluConsultaGeneralResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error",  "${e.message}")
            AluConsultaGeneralResult.Failure(error)
        }
    }
}