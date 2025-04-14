package org.itb.sga.features.common.account

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Account
import org.itb.sga.data.network.AccountResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.AccountForm
import org.itb.sga.data.network.form.UpdateValue
import org.itb.sga.data.network.reportes.DjangoModelItem

class AccountService(
    private val client: HttpClient
) {
    suspend fun fetchAccount(id: Int): AccountResult {
        return try {
            val response = client.get("${SERVER_URL}api_rest?action=account&id=$id")

            if (response.status == HttpStatusCode.OK) {
//                logInfo("account", "Response: ${response.bodyAsText()}")
                val account = response.body<Account>()
                AccountResult.Success(account)
            } else {
                val error = response.body<Error>()
                AccountResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error","Error inesperado: ${e.message}")
            AccountResult.Failure(error)
        }
    }

    suspend fun fetchResidencia(id: Int?, action: String): List<DjangoModelItem> {
        val response = client.get("${SERVER_URL}api_rest?action=$action&id=$id")
        return response.body<List<DjangoModelItem>>()
    }

    suspend fun updateAccount(client: HttpClient, form: AccountForm): Response {
        return try {
            val response = client.post("${SERVER_URL}api_rest?action=account") {
                contentType(ContentType.Application.Json)
                setBody(form)
            }
            response.body<Response>()
        } catch (e: Exception) {
            Response("error", "Error inesperado: ${e.message}")
        }
    }
}