package org.itb.sga.ui.components.alerts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MyWarningAlert(
    titulo: String,
    mensaje: String,
    onDismiss: () -> Unit,
    showAlert: Boolean
) {
    AnimatedVisibility(
        visible = showAlert,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            modifier = Modifier,
            icon = {
                Icon(
                    imageVector = Icons.Filled.PriorityHigh,
                    contentDescription = "",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            title = {
                Text(
                    text = titulo,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = mensaje,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface

                )
            },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Aceptar",
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
//            containerColor = MaterialTheme.colorScheme.errorContainer,
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        )
    }

}
