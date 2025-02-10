package org.example.aok.core

import org.example.aok.features.student.pago_online.PagoOnlineViewModel
import org.example.aok.features.student.pago_online.PayData

actual object PaymentezSDK {
    private val viewModel = PagoOnlineViewModel()

//    actual suspend fun processPayment(context: Any, uid: String, email: String, amount: Double) {
//        // Lógica específica para iOS (no se usa un Context)
//        println("Processing payment for $email with amount $amount")
//    }
    actual suspend fun createCardToken(
        payData: PayData,
        contextProvider: ContextProvider
    ): PaymentezToken {
        TODO("Not yet implemented")
    }
}

actual class PaymentezToken actual constructor(tokenId: String) {
    actual val id: String
        get() = TODO("Not yet implemented")
}
