package org.itb.sga.features.common.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.itb.sga.core.ROUTES
import org.itb.sga.core.capitalizeWords
import org.itb.sga.data.network.GrupoModulo
import org.itb.sga.data.network.Modulo
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyConfirmAlert
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.alerts.MySuccessAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen
import org.itb.sga.ui.components.shimmer.ShimmerFormLoadingAnimation


@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val userData by loginViewModel.userData.collectAsState()
    userData?.idPersona?.let { id ->
        LaunchedEffect(id) {
            homeViewModel.onloadHome(id)
        }
    }
    userData?.let { user ->
        DashBoardScreen(
            title = user.nombre,
            navController = navController,
            content = {
                Screen(navController, homeViewModel, loginViewModel)
            },
            homeViewModel = homeViewModel,
            loginViewModel = loginViewModel
        )
    } ?: run {
        MyCircularProgressIndicator()
    }
}

@Composable
fun Screen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val homeData by homeViewModel.homeData.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val response by homeViewModel.response.collectAsState(null)

    homeViewModel.actualPageRestart()

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isLoading) {
            ShimmerFormLoadingAnimation(20)
        } else {
            homeData?.grupoModulos?.let { grupoModulos ->
                grupoModulos.forEach { item ->
                    GrupoItem(item, navController, homeViewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } ?: run {
                MyCircularProgressIndicator()
            }
        }
    }

    response?.let {
        if (it.status == "success") {
            MySuccessAlert(
                titulo = "Realizado",
                mensaje = it.message,
                onDismiss = {
                    homeViewModel.clearResponse()
                },
                showAlert = true
            )
        } else {
            MyErrorAlert(
                titulo = "Error",
                mensaje = it.message,
                onDismiss = {
                    homeViewModel.clearResponse()
                },
                showAlert = true
            )
        }
    }

    val showSaveCredentials by homeViewModel.showSaveCredentials.collectAsState(initial = false)

    LaunchedEffect(loginViewModel) {
        homeViewModel.confirmCredentialsLogin(loginViewModel)
    }

    if (showSaveCredentials) {
        MyConfirmAlert(
            titulo = "¿Desea continuar?",
            mensaje = "Guardar credenciales de inicio de sesión para uso de biométrico",
            onCancel = {
                homeViewModel.updateShowSaveCredentials(false)
            },
            onConfirm = {
                homeViewModel.updateShowSaveCredentials(false)
                homeViewModel.saveCredentialsLogin(loginViewModel)
            },
            showAlert = true
        )
    }
}

@Composable
fun GrupoItem(
    grupo: GrupoModulo,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { expanded = !expanded }
        ) {
            AnimatedContent(targetState = expanded) { isExpanded ->

            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = grupo.grupo.capitalizeWords(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }}
        Spacer(modifier = Modifier.height(4.dp))

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            panelModulos(
                Modifier,
                grupo.modulos,
                navController,
                homeViewModel
            )
        }
    }
}

@Composable
fun panelModulos(
    modifier: Modifier = Modifier,
    modulos: List<Modulo>,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    homeViewModel.changeFastSearch(true)

    val modulosFiltrados = if (searchQuery.isNotEmpty()) {
        modulos.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
    } else {
        modulos
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(modulosFiltrados) { modulo ->
            cardModulo(modulo = modulo, navController = navController, homeViewModel = homeViewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun cardModulo(
    modulo: Modulo,
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            if (modulo.url in ROUTES) {
                navController.navigate(modulo.url)
            } else {
                navController.navigate("404")
            }
            homeViewModel.clearSearchQuery()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = modulo.img.let { "https://sga.itb.edu.ec$it" },
                contentDescription = modulo.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(64.dp)
                    .aspectRatio(1/1f)
                    .padding(8.dp)
            )

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = modulo.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = modulo.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

    }
}