import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomLinearProgressIndicator(
    progress: Float, // Valor de progreso entre 0 y 1
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary, // Color de la barra de progreso
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant, // Color de fondo de la barra
    height: Dp = 8.dp, // Altura de la barra
    cornerRadius: Dp = 4.dp // Radio de las esquinas redondeadas
) {
    // Animar el valor de progreso
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 50) // Duración de la animación
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(trackColor) // Fondo de la barra (track)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = animatedProgress)
                .fillMaxHeight()
                .background(color) // Color de la barra de progreso
        )
    }
}
