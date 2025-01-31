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
import android.content.Intent
import android.net.Uri
import io.github.vinceglb.filekit.core.FileKit
//import org.example.aok.core.BiometricAuth
import org.example.aok.features.common.login.LoginViewModel

class MainActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel

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
//                val biometricAuth = BiometricAuth(this)


                setContent {
//                    val loginViewModel = LoginViewModel(biometricAuth)
                    val loginViewModel = LoginViewModel()
                    val homeViewModel = HomeViewModel(urlOpener)
                    App(homeViewModel, loginViewModel)
                }
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }
}