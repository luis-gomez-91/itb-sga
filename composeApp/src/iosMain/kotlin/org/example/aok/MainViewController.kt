package org.example.aok

import androidx.compose.ui.window.ComposeUIViewController
import dev.icerock.moko.biometry.BiometryAuthenticator
//import org.example.aok.core.BiometricAuth
import org.example.aok.core.PDFOpenerIOS
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel

fun MainViewController() = ComposeUIViewController {
//    App()
    val pdfOpener = PDFOpenerIOS()
    val homeViewModel = HomeViewModel(pdfOpener)
    val biometricAuth = BiometryAuthenticator()
    val loginViewModel = LoginViewModel(biometricAuth)

    App(
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}