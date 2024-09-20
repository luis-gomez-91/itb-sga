package org.example.aok.features.common.home

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.example.aok.data.network.Error
import org.example.aok.data.network.Home
import org.example.aok.data.network.HomeResult

class HomeService(
    private val client: HttpClient
) {
    suspend fun fetchHome(id: Int): HomeResult {
        return try {
            val response = client.get("https://sga.itb.edu.ec/api_rest?action=panel&id=$id")

            if (response.status == HttpStatusCode.OK) {
                val home = response.body<Home>()
                HomeResult.Success(home)
            } else {
                val error = response.body<Error>()
                HomeResult.Failure(error)
            }
        } catch (e: Exception) {
            val error = Error("Error inesperado: ${e.message}")
            HomeResult.Failure(error)
        }
    }
}