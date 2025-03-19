package org.itb.sga.features.student.alu_cronograma


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.AluCronograma
import org.itb.sga.data.network.AluCronogramaResult
import org.itb.sga.data.network.Error

class AluCronogramaService(
    private val client: HttpClient
) {
    suspend fun fetchAluCronograma(id: Int): AluCronogramaResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_cronograma&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluCronograma>>()
                AluCronogramaResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluCronogramaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error",  "${e.message}")
            AluCronogramaResult.Failure(error)
        }
    }
}

