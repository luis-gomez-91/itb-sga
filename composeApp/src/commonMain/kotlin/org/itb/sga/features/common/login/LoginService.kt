package org.itb.sga.features.common.login

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.itb.sga.core.SERVER_URL
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.Login
import org.itb.sga.data.network.LoginResult
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.AppUpdateForm
import org.itb.sga.data.network.form.RequestPasswordRecoveryForm

class LoginService(
private val client: HttpClient
) {
    suspend fun fetchLogin(user: String, password: String, userId: Int? = null): LoginResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=login&username=$user&password=$password&id=$userId")

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
            logInfo("error", "ERROR: ${e.message}")
            e.printStackTrace()
            Response(status = "error", message = "Exception occurred: ${e.message}")
        }
    }

    suspend fun fetchLastVersionApp(form: AppUpdateForm): Response {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=fetchLastVersionApp&platform=${form}")
            response.body<Response>()

        } catch (e: Exception) {
            Response("error", "Error inesperado: ${e.message}")
        }
    }
}
