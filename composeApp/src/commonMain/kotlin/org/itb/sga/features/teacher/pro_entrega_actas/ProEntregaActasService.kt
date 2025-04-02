package org.itb.sga.features.teacher.pro_entrega_actas

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.ProEntregaActasResult
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.EntregarActaForm
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActas

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

    suspend fun sendActa(client: HttpClient, form: EntregarActaForm): Response {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=pro_entrega_acta") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<Response>()
            } else {
                response.body<Response>()
            }
        } catch (e: Exception) {
            Response("Error", "Error inesperado: ${e.message}")
        }
    }
}