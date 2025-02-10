package org.example.aok

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.paymentez.android.Paymentez
import dev.icerock.moko.biometry.compose.BindBiometryAuthenticatorEffect
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.mvvm.getViewModel
import io.github.vinceglb.filekit.core.FileKit
import org.example.aok.core.AndroidContextProvider
import org.example.aok.core.URLOpenerAndroid
import org.example.aok.data.database.AokRepository
import org.example.aok.data.database.getAokDatabase
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Paymentez.setEnvironment(
            true,
//            "ITB-EC-CLIENT",
//            "YBXZU0UAcW6PcRhiqqdss3NequjylE"
            "KRISTY-EC-CLIENT",
            "8UlxeLTIS3PMHICYqr5zaKdhRliEgZ"
        )


        super.onCreate(savedInstanceState)
        FileKit.init(this)
        supportActionBar?.hide()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val repository = AokRepository(getAokDatabase(context = applicationContext))
        val contextProvider = AndroidContextProvider(this)

        setContent {
            Surface(color = Color.Transparent) {

                val urlOpener = URLOpenerAndroid(this)
                val homeViewModel = HomeViewModel(urlOpener, repository, contextProvider)
                val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()


                loginViewModel = getViewModel {
                    LoginViewModel(
                        biometryAuthenticator = biometryFactory.createBiometryAuthenticator()
                    )
                }
                BindBiometryAuthenticatorEffect(loginViewModel.biometryAuthenticator)

                App(
                    homeViewModel = homeViewModel,
                    loginViewModel = loginViewModel,
                    aokRepository = repository
                )
            }
        }

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }
}
