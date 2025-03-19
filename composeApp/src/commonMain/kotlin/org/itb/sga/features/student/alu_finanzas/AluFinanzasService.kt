package org.itb.sga.features.student.alu_finanzas

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Rubro
import org.itb.sga.data.network.AluFinanzasResult
import org.itb.sga.data.network.Error

class AluFinanzasService(
    private val client: HttpClient
) {
    suspend fun fetchAluFinanzas(id: Int): AluFinanzasResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=alu_finanzas&id=$id")

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