package org.example.aok

import androidx.compose.ui.window.ComposeUIViewController
import org.example.aok.core.PDFOpenerIOS
import org.example.aok.features.common.home.HomeViewModel

fun MainViewController() = ComposeUIViewController {
//    App()
    val pdfOpener = PDFOpenerIOS()
    val homeViewModel = HomeViewModel(pdfOpener)
    App(homeViewModel = homeViewModel)
}