package org.example.aok

import androidx.compose.ui.window.ComposeUIViewController
import dev.icerock.moko.biometry.BiometryAuthenticator
import org.example.aok.core.PDFOpenerIOS
import org.example.aok.data.database.getDatabaseBuilder
import org.example.aok.data.database.getRoomDatabase
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel

fun MainViewController() = ComposeUIViewController {
    val pdfOpener = PDFOpenerIOS()
    val biometricAuth = BiometryAuthenticator()

    val builder = getDatabaseBuilder()
    val aokDatabase = getRoomDatabase(builder)

    val loginViewModel = LoginViewModel(biometricAuth)
    val homeViewModel = HomeViewModel(pdfOpener, aokDatabase)

    App(
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}