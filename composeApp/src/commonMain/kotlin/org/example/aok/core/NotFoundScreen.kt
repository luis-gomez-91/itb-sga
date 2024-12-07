package org.example.aok.core

import androidx.compose.runtime.Composable
import org.example.aok.ui.components.alerts.MyWarningAlert

@Composable
fun NotFoundScreen(onNavigateBack: () -> Unit) {
    MyWarningAlert(
        titulo = "Página no encontrada",
        mensaje = "La funcionalidad que buscas no está disponible actualmente.",
        onDismiss = {
            onNavigateBack()
        },
        showAlert = true
    )
}