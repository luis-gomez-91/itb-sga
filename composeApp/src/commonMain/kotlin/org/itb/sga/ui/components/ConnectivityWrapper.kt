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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import org.itb.sga.features.common.home.HomeViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun ConnectivityWrapper(
    isConnected: Boolean,
    homeViewModel: HomeViewModel,
    content: @Composable () -> Unit
) {
    val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)
    var imageLogo by remember { mutableStateOf(Res.drawable.logo) }

    LaunchedEffect(selectedTheme) {
        imageLogo = if (selectedTheme?.dark == true) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }
    }

    if (isConnected) {
        content()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageLogo),
                contentDescription = "logo",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Esta aplicación requiere acceso a Internet.\n" +
                        "Activa tus datos móviles o Wi-Fi para continuar.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
