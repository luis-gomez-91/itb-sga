package org.example.aok.ui.components.alerts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MyAlert(
    title: String,
    text: @Composable () -> Unit,
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

            },
            title = {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            text = {
                text()
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
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            shape = AlertDialogDefaults.shape,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        )
    }

}
