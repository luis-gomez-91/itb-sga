package org.example.aok.features.common.account

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.example.aok.core.logInfo
import org.example.aok.data.network.Account
import org.example.aok.data.network.AccountResult
import org.example.aok.data.network.Error

class AccountService(
    private val client: HttpClient
) {
    suspend fun fetchAccount(id: Int): AccountResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=account&id=$id")

            if (response.status == HttpStatusCode.OK) {
//                logInfo("account", "Response: ${response.bodyAsText()}")
                val account = response.body<Account>()
                AccountResult.Success(account)
            } else {
                val error = response.body<Error>()
                AccountResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            AccountResult.Failure(error)
        }
    }
}