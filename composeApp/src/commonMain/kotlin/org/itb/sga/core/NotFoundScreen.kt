package org.itb.sga.core

import androidx.compose.runtime.Composable
import org.itb.sga.ui.components.alerts.MyWarningAlert

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