package org.example.aok.features.teacher.pro_entrega_actas

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.core.SERVER_URL
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProEntregaActasResult
import org.example.aok.data.network.pro_entrega_actas.ProEntregaActas

class ProEntregaActasService(
    private val client: HttpClient
) {
    suspend fun fetchProEntregaActas(idDocente: Int, idPeriodo: Int): ProEntregaActasResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=pro_entrega_acta&id=${idDocente}&periodo=${idPeriodo}")

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<ProEntregaActas>>()
                ProEntregaActasResult.Success(data)
            } else {
                val error = response.body<Error>()
                ProEntregaActasResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ProEntregaActasResult.Failure(error)
        }
    }
}