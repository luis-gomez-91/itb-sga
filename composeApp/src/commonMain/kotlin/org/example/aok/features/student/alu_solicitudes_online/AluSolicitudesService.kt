package org.example.aok.features.student.alu_solicitudes_online

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.AluSolicitud
import org.example.aok.data.network.AluSolicitudDepartamentos
import org.example.aok.data.network.AluSolicitudesResult
import org.example.aok.data.network.Error

class AluSolicitudesService(
    private val client: HttpClient
) {
    suspend fun fetchAluSolicitudes(id: Int): AluSolicitudesResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=solicitudonline&id=$id")
            println(response.bodyAsText())

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<List<AluSolicitud>>()
                AluSolicitudesResult.Success(data)
            } else {
                val error = response.body<Error>()
                AluSolicitudesResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            AluSolicitudesResult.Failure(error)
        }
    }

    suspend fun  fetchTipoEspecies(): List<AluSolicitudDepartamentos> {
        val response = client.get("https://sga.itb.edu.ec/api_rest?action=solicitudonline&a=fetch_tipo_especies")
        return response.body()
    }
}