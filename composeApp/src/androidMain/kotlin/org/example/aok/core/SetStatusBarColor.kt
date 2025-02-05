package org.example.aok.core

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun SetStatusBarColors(isDarkTheme: Boolean) {
    val view = LocalView.current
    val context = LocalContext.current

    LaunchedEffect(isDarkTheme) {
        val window = (context as? android.app.Activity)?.window
        window?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.statusBarColor = Color.Transparent.toArgb()
            }

            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
            }
            WindowCompat.setDecorFitsSystemWindows(it, false)

        }
    }
}