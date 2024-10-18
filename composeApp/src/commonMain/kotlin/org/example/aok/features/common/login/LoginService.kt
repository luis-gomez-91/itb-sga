package org.example.aok.features.common.login

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Error
import org.example.aok.data.network.Login
import org.example.aok.data.network.LoginResult

class LoginService(
private val client: HttpClient
) {
    suspend fun fetchLogin(user: String, password: String): LoginResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=login&username=$user&password=$password")

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
}
