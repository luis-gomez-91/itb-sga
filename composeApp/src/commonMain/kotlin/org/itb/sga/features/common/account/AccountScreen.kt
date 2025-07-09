package org.itb.sga.features.common.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.Account
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AccountScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    accountViewModel: AccountViewModel
) {
    DashBoardScreen(
        title = "Perfil de usuario",
        navController = navController,
        content = {
            Screen(
                navController,
                accountViewModel,
                homeViewModel,
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    homeViewModel: HomeViewModel
) {
    val data by accountViewModel.data.collectAsState()
    val error by homeViewModel.error.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value?.persona?.idPersona?.let {
            accountViewModel.onloadAccount(it, homeViewModel)
        }
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        data?.let { persona ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    dataPersona(persona, accountViewModel, navController)
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MyFilledTonalButton(
                        text = "Editar información",
                        icon = Icons.Filled.EditNote,
                        enabled = true,
                        onClickAction = {
                            navController.navigate("account_edit")
                        },
                        buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                        textColor = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
    error?.let {
        MyErrorAlert(
            titulo = it.title,
            mensaje = it.error,
            onDismiss = {
                homeViewModel.clearError()
                navController.popBackStack()
            },
            showAlert = true
        )
    }
}

@Composable
fun dataPersona(
    persona: Account,
    accountViewModel: AccountViewModel,
    navController: NavHostController
) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        AsyncImage(
            model = persona.foto,
            contentDescription = "Foto perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(1 / 1f)
                .padding(8.dp)
                .clip(CircleShape)
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = persona.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatoText("${persona.tipoIdentificacion}:", persona.identificacion),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { InformacionPersonal(persona, accountViewModel, navController) }
        item { InformacionContacto(persona, accountViewModel, navController) }
        item { InformacionDomicilio(persona, accountViewModel, navController) }
        item { InformacionAdicional(persona, accountViewModel, navController) }
    }
}

@Composable
fun InformacionPersonal(persona: Account, accountViewModel: AccountViewModel, navController: NavHostController) {
    val list: List<Map<String, String?>> = listOf(
        mapOf("Usuario:" to persona.username),
        mapOf("Sexo:" to persona.sexo),
        mapOf("Fecha de nacimiento:" to persona.fechaNacimiento),
        mapOf("Nacionalidad:" to persona.nacionalidad),
        mapOf("Extranjero:" to if (persona.extranjero) "SI" else "NO"),
        mapOf("Tipo de sangre:" to persona.nombreTipoSangre),
    )
    MostrarDatos(
        "Información personal",
        list,
        {
            accountViewModel.updateTab(0)
            navController.navigate("account_edit")
        }
    )
}

@Composable
fun InformacionContacto(persona: Account, accountViewModel: AccountViewModel, navController: NavHostController) {
    val list: List<Map<String, String?>> = listOf(
        mapOf("Celular:" to persona.celular),
        mapOf("Convencional:" to persona.convencional),
        mapOf("Correo institucional:" to persona.emailinst),
        mapOf("Correo personal:" to persona.email),
    )
    MostrarDatos(
        "Información de contacto:",
        list,
        {
            accountViewModel.updateTab(1)
            navController.navigate("account_edit")
        }
    )
}

@Composable
fun InformacionDomicilio(persona: Account, accountViewModel: AccountViewModel, navController: NavHostController) {
    val list: List<Map<String, String?>> = listOf(
        mapOf("Provincia:" to persona.nombreProvinciaResidencia),
        mapOf("Canton:" to persona.nombreCantonResidencia),
        mapOf("Parroquia:" to persona.nombreParroquia),
        mapOf("Sector:" to persona.sector),
        mapOf("Calle principal:" to persona.domicilioCallePrincipal),
        mapOf("Calle secundaria:" to persona.domicilioCalleSecundaria),
        mapOf("Número de domicilio:" to persona.domicilio_numero),
    )
    MostrarDatos(
        "Lugar  de residencia",
        list,
        {
            accountViewModel.updateTab(2)
            navController.navigate("account_edit")
        }
    )
}

@Composable
fun InformacionAdicional(persona: Account, accountViewModel: AccountViewModel, navController: NavHostController) {
    val list: List<Map<String, String?>> = listOf(
        mapOf("Provincia de nacimiento:" to persona.provinciaNacimiento),
        mapOf("Cantón de nacimiento:" to persona.cantonNacimiento),
        mapOf("Madre:" to persona.madre),
        mapOf("Padre:" to persona.padre),
    )
    MostrarDatos(
        "Información adicional",
        list,
        {
            accountViewModel.updateTab(3)
            navController.navigate("account_edit")
        }
    )
}

@Composable
fun MostrarDatos(
    titulo: String,
    list: List<Map<String, String?>>,
    onclick: () -> Unit
) {
    MyCard (
        onClick = {
            onclick()
        }
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            list.forEach { map ->
                map.entries.forEach { entry ->
                    val key = entry.key
                    val value = entry.value

                    value?.let {
                        Text(
                            text = formatoText(key, value),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

