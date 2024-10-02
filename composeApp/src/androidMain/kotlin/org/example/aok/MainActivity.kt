package org.example.aok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import org.example.aok.core.PDFOpenerAndroid
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.student.facturacion_electronica.AluFacturacionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = Color.Transparent) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
//                window.navigationBarColor = MaterialTheme.colorScheme.secondaryContainer.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, window.decorView)
                val darkTheme = isSystemInDarkTheme()
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
                val pdfOpener = PDFOpenerAndroid(this)

                setContent {
                    val homeViewModel = HomeViewModel(pdfOpener)
                    App(homeViewModel)
                }

//                App()
            }
        }
    }
}