package org.itb.sga.core

//@Composable
//actual fun SetStatusBarColors(isDarkTheme: Boolean) {
//    val view = LocalView.current
//    val context = LocalContext.current
//
//    LaunchedEffect(isDarkTheme) {
//        val window = (context as? android.app.Activity)?.window
//        window?.let {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                it.statusBarColor = Color.Transparent.toArgb()
//            }
//
//            WindowCompat.getInsetsController(it, view).apply {
//                isAppearanceLightStatusBars = !isDarkTheme
//            }
//            WindowCompat.setDecorFitsSystemWindows(it, false)
//
//        }
//    }
//}