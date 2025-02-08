package org.example.aok.core

import org.example.aok.data.network.DatosFacturacion


expect object PaymentezSDK {
    suspend fun processPayment(mContext: Any, uid: String, email: String, amount: Double)

    suspend fun createCharge(
        uid: String,
        sessionId: String?,
        token: String,
        amount: Double,
        devReference: String,
        description: String
    ): Result<String>

    fun getUniqToken(authTimestamp: String, appSecretKey: String): String
    fun getAuthToken(appCode: String, appSecretKey: String): String
    fun getHash(message: String): String
    fun paymentezDebitJson(
        customer: DatosFacturacion,
        sessionId: String?,
        token: String,
        amount: Double,
        devReference: String,
        description: String
    ): String

    suspend fun doPostRequest(
        url: String,
        json: String
    ): Map<String, String>

    suspend fun doGetRequest(url: String): Map<String, String>


}

interface ContextProvider {
    fun getContext(): Any
}