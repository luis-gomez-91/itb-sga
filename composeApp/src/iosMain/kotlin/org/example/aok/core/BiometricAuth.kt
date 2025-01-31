package org.example.aok.core

//import platform.LocalAuthentication.LAContext
//import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
//import platform.Foundation.NSError
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//actual class BiometricAuth {
//
//    actual suspend fun authenticate(callback: (Boolean, String?) -> Unit) {
//        withContext(Dispatchers.Main) {
//            val context = LAContext()
//            val reason = "Autenticación biométrica requerida"
//
//            context.evaluatePolicy(
//                LAPolicyDeviceOwnerAuthenticationWithBiometrics,
//                localizedReason = reason
//            ) { success, error ->
//                if (success) {
//                    callback(true, null)
//                } else {
//                    callback(false, error?.localizedDescription ?: "Error en la autenticación")
//                }
//            }
//        }
//    }
//}