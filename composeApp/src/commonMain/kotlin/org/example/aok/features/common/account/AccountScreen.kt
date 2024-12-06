package org.example.aok.features.common.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.example.aok.core.MainViewModel
import org.example.aok.data.network.Account
import org.example.aok.data.network.Persona
import org.example.aok.features.common.home.GrupoItem
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyErrorAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AccountScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    accountViewModel: AccountViewModel
) {
    DashBoardScreen(
        title = "Perfil",
        navController = navController,
        content = {
            Screen(
                navController,
                accountViewModel,
                homeViewModel,
            )
        },
        mainViewModel = mainViewModel,
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
        accountViewModel.onloadAccount(homeViewModel.homeData.value!!.persona.idPersona, homeViewModel)
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        data?.let { persona ->
            dataPersona(navController, persona)
        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MyCircularProgressIndicator()
            }
        }

        if (error != null) {
            MyErrorAlert(
                titulo = error!!.title,
                mensaje = error!!.error,
                onDismiss = {
                    homeViewModel.clearError()
                    navController.popBackStack()
                },
                showAlert = true
            )
        }
    }
}

@Composable
fun dataPersona(
    navController: NavHostController,
    persona: Account,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = persona.foto,
            contentDescription = "Foto perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .aspectRatio(1 / 1f)
                .padding(8.dp)
                .clip(CircleShape)
        )
        Text(
            text = persona.nombre,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = persona.identificacion,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }

    Column (
        modifier = Modifier
            .fillMaxWidth().padding(top = 8.dp)
    ) {
        obtenerData(persona).forEach { map ->
            map.entries.forEach { entry ->
                val key = entry.key
                val value = entry.value
                Row (
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 2.dp)
                ){
                    Text(
                        text = "${key}:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = value.toString(),
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

//                Spacer(modifier = Modifier.height(4.dp))
//                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)
            }
        }


    }
}


fun obtenerData(persona: Account): List<Map<String, Any>> {
    val nacionalidad = if (persona.extranjero) "Si" else "No"
    val provinciaNacimiento = if (persona.provinciaNacimiento != null) persona.provinciaNacimiento.toString() else ""
    val cantonNacimiento = if (persona.cantonNacimiento != null) persona.cantonNacimiento.toString() else ""
    val email = if (persona.email != null) persona.email.toString() else ""
    val convencional = if (persona.convencional != null) persona.convencional.toString() else ""

    val list: List<Map<String, Any>> = listOf(
        mapOf("Correo institucional" to persona.emailinst),
        mapOf("Correo personal" to email),
        mapOf("Teléfono celular" to persona.celular),
        mapOf("Teléfono convencional" to convencional),

        mapOf("Extranjero" to if (persona.extranjero) "Si" else "No"),
        mapOf("Nacionalidad" to nacionalidad),
        mapOf("Provincia nacimiento" to provinciaNacimiento),
        mapOf("Canton nacimiento" to cantonNacimiento),
        mapOf("Sexo" to persona.sexo),
    )

    return list
}