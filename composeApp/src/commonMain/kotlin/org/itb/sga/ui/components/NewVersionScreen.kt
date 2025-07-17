package org.itb.sga.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Upload
import org.itb.sga.core.openPlayStoreOrAppStore
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


@Composable
fun NewVersionScreen(
    imageLogo: DrawableResource
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 0.dp, start = 32.dp, end = 32.dp, bottom = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageLogo),
            contentDescription = "logo",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "¡Una nueva versión está disponible! Actualiza la app para seguir disfrutando de las mejores funciones.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        MyFilledTonalButton(
            text = "Actualizar",
            buttonColor = MaterialTheme.colorScheme.primaryContainer,
            textColor = MaterialTheme.colorScheme.primary,
            icon = TablerIcons.Upload,
            onClickAction = { openPlayStoreOrAppStore() },
            iconSize = 32.dp,
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}