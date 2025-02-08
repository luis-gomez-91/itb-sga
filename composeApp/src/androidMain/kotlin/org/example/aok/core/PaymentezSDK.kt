package org.example.aok.core

import android.content.Context
import com.google.android.gms.common.util.Hex
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.aok.data.network.DatosFacturacion
import org.example.aok.features.student.alu_documentos.client
import java.util.Base64
import java.security.MessageDigest

actual object PaymentezSDK {

    actual suspend fun processPayment(mContext: Any, uid: String, email: String, amount: Double) {

    }

    actual suspend fun createCharge(
        uid: String,
        sessionId: String?,
        token: String,
        amount: Double,
        devReference: String,
        description: String
    ): Result<String> {
        return try {
            val response: HttpResponse = client.post("https://ccapi-stg.paymentez.com/v2/transaction/debit") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "uid" to uid,
                        "session_id" to sessionId,
                        "token" to token,
                        "amount" to amount,
                        "dev_reference" to devReference,
                        "description" to description
                    )
                )
            }
            if (response.status.isSuccess()) {
                Result.success(response.bodyAsText()) // Retorna el JSON como String
            } else {
                Result.failure(Exception("Error ${response.status.value}: ${response.bodyAsText()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    actual fun getUniqToken(authTimestamp: String, appSecretKey: String): String {
        val uniqTokenString = appSecretKey + authTimestamp
        return getHash(uniqTokenString)
    }

    actual fun getAuthToken(appCode: String, appSecretKey: String): String {
        val tsLong = System.currentTimeMillis() / 1000
        val authTimestamp = tsLong.toString()
        val stringAuthToken = "$appCode;$authTimestamp;${getUniqToken(authTimestamp, appSecretKey)}"
        return Base64.getEncoder().encodeToString(stringAuthToken.toByteArray())
    }

    actual fun getHash(message: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(message.toByteArray())

        // Convertir el arreglo de bytes a una cadena hexadecimal
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    actual fun paymentezDebitJson(customer: DatosFacturacion, sessionId: String?, token: String, amount: Double, devReference: String, description: String): String {
        val sessionRow = sessionId?.let { "\"session_id\": \"$it\"," } ?: ""
        return """{
            $sessionRow
            "user": {
                "id": "${customer.cedula}",
                "email": "${customer.correo}"
            },
            "order": {
                "code": "123",
                "amount": $amount,
                "description": "$description",
                "dev_reference": "$devReference",
                "vat": 0.00
            },
            "card": {
                "token": "$token"
            }
        }"""
    }

    actual suspend fun doPostRequest(url: String, json: String): Map<String, String> {
        val jsonResponse = "{}"
        val authToken = getAuthToken(System.getenv("KRISTY-EC-CLIENT"), System.getenv("8UlxeLTIS3PMHICYqr5zaKdhRliEgZ"))
        val response: HttpResponse = client.post(url) {
            header("Auth-Token", authToken)
            contentType(ContentType.Application.Json)
            setBody(json)
        }
        val mapResult = mutableMapOf<String, String>()
        mapResult["RESPONSE_HTTP_CODE"] = response.status.value.toString()
        mapResult["RESPONSE_JSON"] = response.bodyAsText()
        return mapResult
    }

    actual suspend fun doGetRequest(url: String): Map<String, String> {
        val jsonResponse = "{}"
        val authToken = getAuthToken(System.getenv("KRISTY-EC-CLIENT"), System.getenv("8UlxeLTIS3PMHICYqr5zaKdhRliEgZ"))
        val response: HttpResponse = client.get(url) {
            header("Auth-Token", authToken)
        }
        val mapResult = mutableMapOf<String, String>()
        mapResult["RESPONSE_HTTP_CODE"] = response.status.value.toString()
        mapResult["RESPONSE_JSON"] = response.bodyAsText()
        return mapResult
    }


}

class AndroidContextProvider(private val context: Context) : ContextProvider {
    override fun getContext(): Context = context
}