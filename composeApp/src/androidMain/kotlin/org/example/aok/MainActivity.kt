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
import org.example.aok.core.URLOpenerAndroid
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.student.facturacion_electronica.AluFacturacionViewModel
import android.content.Intent
import android.net.Uri
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
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
                val urlOpener = URLOpenerAndroid(this)

                setContent {
                    val homeViewModel = HomeViewModel(urlOpener)
                    App(homeViewModel)
                }

//                App()
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }
}