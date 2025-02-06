package org.example.aok

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import dev.icerock.moko.biometry.BiometryAuthenticator
import org.example.aok.core.PDFOpenerIOS
import org.example.aok.data.database.AokRepository
import org.example.aok.data.database.getAokDatabase
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel

fun MainViewController() = ComposeUIViewController {
    val pdfOpener = PDFOpenerIOS()
    val biometricAuth = BiometryAuthenticator()
    val loginViewModel = LoginViewModel(biometricAuth)
    val aokRepository = remember {
        AokRepository(getAokDatabase())
    }
    val homeViewModel = HomeViewModel(pdfOpener, aokRepository)

    App(
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel,
        aokRepository = aokRepository
    )
}