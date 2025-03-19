package org.itb.sga

import androidx.compose.ui.window.ComposeUIViewController
import dev.icerock.moko.biometry.BiometryAuthenticator
import org.itb.sga.core.PDFOpenerIOS
import org.itb.sga.data.database.getDatabaseBuilder
import org.itb.sga.data.database.getRoomDatabase
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel

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