package org.example.aok

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.mvvm.getViewModel
import io.github.vinceglb.filekit.core.FileKit
import org.example.aok.core.URLOpenerAndroid
import org.example.aok.data.database.AokDatabase
import org.example.aok.data.database.getDatabaseBuilder
import org.example.aok.data.database.getRoomDatabase
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var aokDatabase: AokDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val builder = getDatabaseBuilder(this)
        aokDatabase = getRoomDatabase(builder)

        FileKit.init(this)
        supportActionBar?.hide()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Surface(color = Color.Transparent) {

                val urlOpener = URLOpenerAndroid(this)
                val homeViewModel = HomeViewModel(urlOpener, aokDatabase)
                val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()


                loginViewModel = getViewModel {
                    LoginViewModel(
                        biometryAuthenticator = biometryFactory.createBiometryAuthenticator()
                    )
                }
                BindBiometryAuthenticatorEffect(loginViewModel.biometryAuthenticator)

                App(
                    homeViewModel = homeViewModel,
                    loginViewModel = loginViewModel
                )
            }
        }

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }
}
