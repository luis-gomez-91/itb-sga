// shared/src/commonMain/kotlin/com/example/theme/Theme.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
//import org.example.aok.core.SetStatusBarColors
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.theme.darkColorScheme
import org.itb.sga.ui.theme.lightColorScheme

@Composable
fun AppTheme(
    homeViewModel: HomeViewModel,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)

    LaunchedEffect(Unit) {
        homeViewModel.reloadTheme()
    }

    val isDark = selectedTheme?.let {
        if (it.system) isSystemInDarkTheme() else it.dark
    } ?: darkTheme

    val colorScheme = if (isDark) darkColorScheme() else lightColorScheme()

//    SetStatusBarColors(isDarkTheme = isDark)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
