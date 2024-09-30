package org.example.aok.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


@Composable
fun MyFilledTonalButton(
    text: String = "Ingresar", // Texto opcional
    enabled: Boolean = true, // Estado del botón
    onClickAction: () -> Unit, // Acción al hacer clic
    buttonColor: Color = MaterialTheme.colorScheme.primaryContainer, // Color del botón opcional
    textColor: Color = MaterialTheme.colorScheme.primary // Color del texto opcional
) {
    FilledTonalButton(
        onClick = onClickAction,
//        modifier = Modifier.height(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp), // Esquinas redondeadas
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = buttonColor, // Usar color pasado como parámetro o predeterminado
            contentColor = textColor // Usar color del texto pasado como parámetro o predeterminado
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor // Aplicar el color del texto
        )
    }
}