package org.example.aok.features.common.login

import aok.composeapp.generated.resources.Res
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.aok.core.SERVER_URL
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.Login
import org.example.aok.data.network.LoginResult
import org.example.aok.data.network.Response
import org.example.aok.data.network.form.RequestPasswordRecoveryForm

class LoginService(
private val client: HttpClient
) {
    suspend fun fetchLogin(user: String, password: String): LoginResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=login&username=$user&password=$password")

            if (response.status == HttpStatusCode.OK) {
                val login = response.body<Login>()
                LoginResult.Success(login)
            } else {
                val error = response.body<Error>()
                LoginResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            LoginResult.Failure(error)
        }
    }

    suspend fun requestPasswordRecovery(form: RequestPasswordRecoveryForm): Response {
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
