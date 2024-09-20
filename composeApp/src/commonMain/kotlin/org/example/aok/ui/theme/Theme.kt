// shared/src/commonMain/kotlin/com/example/theme/Theme.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import org.example.aok.ui.theme.darkColorScheme
import org.example.aok.ui.theme.lightColorScheme

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(), // Puedes personalizar la tipografía
        shapes = Shapes(), // Puedes personalizar las formas
        content = content
    )
}
