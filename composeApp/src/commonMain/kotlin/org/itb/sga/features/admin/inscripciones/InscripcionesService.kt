package org.itb.sga.features.admin.inscripciones

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.InscripcionResult
import org.itb.sga.data.network.Inscripciones

class InscripcionesService(
    private val client: HttpClient
) {
    suspend fun fetchInscripciones(search: String, page: Int): InscripcionResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=inscripcion&search=$search&page=${page}")

            if (response.status == HttpStatusCode.OK) {
                val inscripciones = response.body<Inscripciones>()
                InscripcionResult.Success(inscripciones)
            } else {
                val error = response.body<Error>()
                InscripcionResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            InscripcionResult.Failure(error)
        }
    }
}