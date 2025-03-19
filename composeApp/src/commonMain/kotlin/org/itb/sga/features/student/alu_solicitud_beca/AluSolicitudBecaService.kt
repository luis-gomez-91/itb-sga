package org.itb.sga.features.student.alu_solicitud_beca

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.AluSolicitudBecaResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.FichaSocioeconomicaResult
import org.itb.sga.data.network.solicitud_becas.FichaSocioeconomica
import org.itb.sga.data.network.solicitud_becas.SolicitudBeca

class AluSolicitudBecaService(
    private val client: HttpClient
) {
    suspend fun fetchAluSolicitudBeca(id: Int): AluSolicitudBecaResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=beca_solicitud&id=$id")
            println(response.bodyAsText())

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<SolicitudBeca>>()
                AluSolicitudBecaResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluSolicitudBecaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluSolicitudBecaResult.Failure(error)
        }
    }

    suspend fun fetchFichaSocioeconomica(id: Int): FichaSocioeconomicaResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=beca_solicitud&id=$id&a=fichaSocioeconomica")
            println(response.bodyAsText())

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<FichaSocioeconomica>()
                FichaSocioeconomicaResult.Success(data)
            } else {
                val error = response.body<Error>()
                FichaSocioeconomicaResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            FichaSocioeconomicaResult.Failure(error)
        }
    }
}