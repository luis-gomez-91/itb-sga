package org.itb.sga.features.common.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavHostController
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import org.itb.sga.data.domain.ProfileItem
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.MyFilledTonalButton
import org.jetbrains.compose.resources.painterResource

@Composable
fun MainScreen(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val profiles = listOf(
        ProfileItem(text = "Alumno", onclik = { navHostController.navigate("login") }, bgColor = MaterialTheme.colorScheme.primaryContainer, textColor = MaterialTheme.colorScheme.primary),
        ProfileItem(text = "Docente", onclik = { navHostController.navigate("login") }, bgColor = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.secondary ),
        ProfileItem(text = "Nómina", onclik = { navHostController.navigate("payroll") }, bgColor = MaterialTheme.colorScheme.tertiaryContainer, textColor = MaterialTheme.colorScheme.tertiary)
    )

    val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)
    var imageLogo by remember { mutableStateOf(Res.drawable.logo) }

    LaunchedEffect(selectedTheme) {
        imageLogo = if (selectedTheme?.dark == true) {
            Res.drawable.logo_dark
        } else {
            Res.drawable.logo
        }
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        item {
            Image(
                painter = painterResource(imageLogo),
                contentDescription = "logo",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Seleccione perfil",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
        }

        items(profiles) { profile ->
            MyFilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                text = profile.text,
                buttonColor = profile.bgColor,
                textColor = profile.textColor,
                onClickAction = profile.onclik,
                textStyle = MaterialTheme.typography.titleMedium,
                shape = MaterialTheme.shapes.small
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}