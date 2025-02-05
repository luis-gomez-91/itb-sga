package org.example.aok.core

import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIViewController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun SetStatusBarColors(isDarkTheme: Boolean) {
    val context = LocalContext.current

    LaunchedEffect(isDarkTheme) {
        val viewController = context as? UIViewController
        viewController?.let {
            it.preferredStatusBarStyle = if (isDarkTheme) {
                UIStatusBarStyle.UIStatusBarStyleLightContent
            } else {
                UIStatusBarStyle.UIStatusBarStyleDarkContent
            }
            it.setNeedsStatusBarAppearanceUpdate()
        }
    }
}