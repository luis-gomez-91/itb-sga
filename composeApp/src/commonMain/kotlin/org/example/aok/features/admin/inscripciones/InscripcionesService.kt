package org.example.aok.features.admin.inscripciones

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Error
import org.example.aok.data.network.Inscripcion
import org.example.aok.data.network.InscripcionResult
import org.example.aok.data.network.Inscripciones

class InscripcionesService(
    private val client: HttpClient
) {
    suspend fun fetchInscripciones(search: String, from: Int, to: Int): InscripcionResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=inscripcion&search=$search&desde=${from}&hasta=${to}")

            if (response.status == HttpStatusCode.OK) {
                val inscripciones = response.body<Inscripciones>()
                InscripcionResult.Success(inscripciones)
            } else {
                val error = response.body<Error>()
                InscripcionResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            InscripcionResult.Failure(error)
        }
    }
}