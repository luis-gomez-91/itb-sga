package org.example.aok.core

//import platform.LocalAuthentication.LAContext
//import platform.Foundation.NSError
//
//class IOSBiometricAuthenticator : BiometricAuthenticator {
//    override fun authenticate(callback: (Boolean, String?) -> Unit) {
//        val context = LAContext()
//        val reason = "Usa Face ID o Touch ID para autenticarse"
//
//        context.evaluatePolicy(0, reason) { success, error ->
//            if (success) {
//                callback(true, null)
//            } else {
//                callback(false, (error as NSError).localizedDescription)
//            }
//        }
//    }
//}