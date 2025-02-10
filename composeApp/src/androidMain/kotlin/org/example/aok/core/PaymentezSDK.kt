package org.example.aok.core

import android.app.Activity
import android.content.Context
import android.util.Log
import com.paymentez.android.Paymentez
import com.paymentez.android.model.Card
import com.paymentez.android.rest.TokenCallback
import com.paymentez.android.rest.model.PaymentezError
import kotlinx.coroutines.suspendCancellableCoroutine
import org.example.aok.features.student.pago_online.PayData
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual object PaymentezSDK {

    actual suspend fun createCardToken(
        payData: PayData,
        contextProvider: ContextProvider
    ): PaymentezToken = suspendCancellableCoroutine { continuation ->
        try {
            Log.i("prueba", "ENTRO A createCardToken")
            val card = Card(
                payData.cardNumber,
                payData.expiryMonth.toInt(),
                payData.expiryYear.toInt(),
                payData.cvc,
                payData.cardHolderName,
                null, null, null, null, null, null, null, null, null, null, null, null,
                payData.userId
            )

//            Log.i("prueba", "CONTEXTO: ANTES")
//
//            val context = contextProvider.getContext() as? Activity
///            Log.i("prueba", "CONTEXTO: ${context}")
//            if (context is Context) {
                Paymentez.addCard(null, payData.userId, payData.email, card, object : TokenCallback {
                    override fun onSuccess(card: Card) {
                        continuation.resume(PaymentezToken(card.id))
                    }

                    override fun onError(error: PaymentezError) {
                        Log.e("prueba", "Error: ${error.description}")
                        continuation.resumeWithException(Exception(error.description))
                    }
                })
//            } else {
//                Log.e("prueba", "CONTEXTO INVALIDO")
//                continuation.resumeWithException(Exception("Contexto no válido"))
//            }
        } catch (e: Exception) {
            Log.e("prueba", "Error al crear token de tarjeta: ${e.message}", e)
            continuation.resumeWithException(e)
        }

    }
}

actual class PaymentezToken actual constructor(tokenId: String) {
    actual val id: String = tokenId
}

class AndroidContextProvider(private val context: Context) : ContextProvider {
    override fun getContext(): Context = context
}


