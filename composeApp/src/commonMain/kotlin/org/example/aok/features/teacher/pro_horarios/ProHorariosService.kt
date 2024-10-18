package org.example.aok.features.teacher.pro_horarios

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Error
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorariosResult

class ProHorariosService(
    private val client: HttpClient
) {
    suspend fun fetchProHorarios(periodo: Periodo, id: Int): ProHorariosResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=pro_horarios&id=${id}&periodo=${periodo.id}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<ProHorario>>()
                ProHorariosResult.Success(data)
            } else {
                val error = response.body<Error>()
                ProHorariosResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ProHorariosResult.Failure(error)
        }
    }
}