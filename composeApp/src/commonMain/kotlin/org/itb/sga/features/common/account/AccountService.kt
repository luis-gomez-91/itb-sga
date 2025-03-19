package org.itb.sga.features.common.account

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.itb.sga.core.SERVER_URL
import org.itb.sga.data.network.Account
import org.itb.sga.data.network.AccountResult
import org.itb.sga.data.network.Error

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
}