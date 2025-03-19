package org.itb.sga.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.itb.sga.core.formatoText

@Composable
fun MySmallParagraphFormat(
    title: String,
    text: String
) {
    Text(
        text = formatoText(title, text),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun MyMediumParagraphFormat(
    title: String,
    text: String
) {
    Text(
        text = formatoText(title, text),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun MyLargeParagraphFormat(
    title: String,
    text: String
) {
    Text(
        text = formatoText(title, text),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}