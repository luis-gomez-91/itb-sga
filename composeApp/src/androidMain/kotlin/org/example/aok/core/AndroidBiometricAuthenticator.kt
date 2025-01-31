package org.example.aok.core

//import android.content.Context
//import androidx.biometric.BiometricManager
//import androidx.biometric.BiometricPrompt
//import androidx.core.content.ContextCompat
//
//class AndroidBiometricAuthenticator(private val context: Context) : BiometricAuthenticator {
//
//    override fun authenticate(callback: (Boolean, String?) -> Unit) {
//        val executor = ContextCompat.getMainExecutor(context)
//        val biometricPrompt = BiometricPrompt(
//            context as androidx.fragment.app.FragmentActivity,
//            executor,
//            object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    callback(true, null)
//                }
//
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    callback(false, errString.toString())
//                }
//
//                override fun onAuthenticationFailed() {
//                    callback(false, "Autenticación fallida")
//                }
//            }
//        )
//
//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Autenticación Biométrica")
//            .setSubtitle("Usa tu huella dactilar o Face ID para acceder")
//            .setNegativeButtonText("Cancelar")
//            .build()
//
//        biometricPrompt.authenticate(promptInfo)
//    }
//}