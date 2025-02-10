package org.example.aok.core

import org.example.aok.features.student.pago_online.PayData

expect object PaymentezSDK {
    suspend fun createCardToken(payData: PayData, contextProvider: ContextProvider): PaymentezToken

}

interface ContextProvider {
    fun getContext(): Any
}

expect class PaymentezToken(tokenId: String) {
    val id: String
}