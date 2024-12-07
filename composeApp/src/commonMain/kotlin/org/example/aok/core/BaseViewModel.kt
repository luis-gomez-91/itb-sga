package org.example.aok.core

import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.example.aok.data.network.Response

abstract class BaseViewModel(private val client: HttpClient) : ViewModel() {
    abstract val endpoint: String
}


class BaseService(
    private val client: HttpClient
) {
    suspend fun requestPostDispatcher(form: Any): Response {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=postDispatcher") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            if (response.status.isSuccess()) {
                val successResponse = response.body<Response>()
                successResponse
            } else {
                Response(status = "error", message = "Unexpected response status: ${response.status}")
            }
        } catch (e: Exception) {
            logInfo("prueba", "ERROR: ${e.message}")
            e.printStackTrace()
            Response(status = "error", message = "Exception occurred: ${e.message}")
        }
    }


}