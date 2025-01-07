package org.example.aok.ui.components.alerts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.aok.ui.components.MyFilledTonalButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyConfirmAlert(
    titulo: String,
    mensaje: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    showAlert: Boolean
) {
    AnimatedVisibility(
        visible = showAlert,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BasicAlertDialog(
            onDismissRequest = { onCancel() },
            modifier = Modifier
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = AlertDialogDefaults.TonalElevation,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.QuestionMark,
                        contentDescription = "",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            onClick = { onCancel() }
                        ) {
                            Text("Cancelar", style = MaterialTheme.typography.labelMedium)
                        }
                        MyFilledTonalButton(
                            text = "Aceptar",
                            enabled = true,
                            onClickAction = { onConfirm() },
                            buttonColor = MaterialTheme.colorScheme.primary,
                            textColor = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        )
                    }

                }
            }

        }
    }
}