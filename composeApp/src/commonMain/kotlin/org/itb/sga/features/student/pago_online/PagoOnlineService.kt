package org.itb.sga.features.student.pago_online

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.PagoOnline
import org.itb.sga.data.network.PagoOnlineResult

class PagoOnlineService(
    private val client: HttpClient
) {
    suspend fun fetchPagoOnline(id: Int): PagoOnlineResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=online&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val pagoOnline = response.body<PagoOnline>()
                PagoOnlineResult.Success(pagoOnline)
            } else {
                val error = response.body<Error>()
                PagoOnlineResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            PagoOnlineResult.Failure(error)
        }
    }
}