package org.example.aok.core

//import android.content.Context
//import androidx.biometric.BiometricManager
//import androidx.biometric.BiometricPrompt
//import androidx.core.content.ContextCompat
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//actual class BiometricAuth(private val context: Context) {
//
//    actual suspend fun authenticate(callback: (Boolean, String?) -> Unit) {
//        withContext(Dispatchers.Main) {
//            val biometricManager = BiometricManager.from(context)
//            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
//                BiometricManager.BIOMETRIC_SUCCESS) {
//
//                val executor = ContextCompat.getMainExecutor(context)
//                val biometricPrompt = BiometricPrompt(
//                    context as androidx.fragment.app.FragmentActivity,
//                    executor,
//                    object : BiometricPrompt.AuthenticationCallback() {
//                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                            callback(true, null)
//                        }
//
//                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                            callback(false, errString.toString())
//                        }
//
//                        override fun onAuthenticationFailed() {
//                            callback(false, "Autenticación fallida")
//                        }
//                    }
//                )
//
//                val promptInfo = BiometricPrompt.PromptInfo.Builder()
//                    .setTitle("Autenticación Biométrica")
//                    .setSubtitle("Inicia sesión con tu huella o Face ID")
//                    .setNegativeButtonText("Cancelar")
//                    .build()
//
//                biometricPrompt.authenticate(promptInfo)
//
//            } else {
//                callback(false, "La autenticación biométrica no está disponible")
//            }
//        }
//    }
//}