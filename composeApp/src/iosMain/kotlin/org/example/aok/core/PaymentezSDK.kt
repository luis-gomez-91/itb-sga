package org.example.aok.core

import org.example.aok.features.student.pago_online.PagoOnlineViewModel

actual object PaymentezSDK {
    private val viewModel = PagoOnlineViewModel()

    actual suspend fun processPayment(context: Any, uid: String, email: String, amount: Double) {
        // Lógica específica para iOS (no se usa un Context)
        println("Processing payment for $email with amount $amount")
    }
}