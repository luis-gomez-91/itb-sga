package org.itb.sga.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.data.network.Periodo
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.alerts.MyAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    title: String,
    navController: NavHostController,
    content: @Composable () -> Unit = {},
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val showBottomSheet by homeViewModel.showBottomSheet.collectAsState(false)
    val notificaciones by homeViewModel.notificaciones.collectAsState(emptyList())
    val showNotifications by homeViewModel.showNotifications.collectAsState(false)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MyDrawerContent(
                drawerState,
                navController,
                homeViewModel,
                loginViewModel
            )
        },
        content = {
            Scaffold(
                topBar = { MyTopBar(title = title, drawerState = drawerState, homeViewModel = homeViewModel) },
                bottomBar = { MyBottomBar(navController = navController, homeViewModel = homeViewModel) }
            ) { innerPadding ->
                Surface(
                    Modifier
                        .padding(innerPadding)
                        .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 4.dp)
                    ) {
                        content()
                    }
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        homeViewModel.changeBottomSheet()
                    }
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Períodos Académicos",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(homeViewModel.homeData.value?.periodos ?: emptyList()) { periodo ->
                            PeriodoItem(
                                periodo = periodo,
                                homeViewModel = homeViewModel
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            if (notificaciones.isNotEmpty() && showNotifications) {
                MyAlert(
                    title = "Notificaciones ITB",
                    onDismiss = {
                        homeViewModel.changeShowNotifications(false)
                    },
                    showAlert = showNotifications,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    text = {
                        LazyColumn {
                            items(notificaciones) { notificacion ->
                                MyCard(
                                    onClick = { },
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            text = notificacion.notificacion_titulo,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = notificacion.notificacion_descripcion,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        if (notificacion.urls.isNotEmpty()) {
                                            Spacer(Modifier.height(4.dp))
                                            LazyRow(
                                                modifier = Modifier.fillMaxWidth(),
                                            ) {
                                                items(notificacion.urls) { url ->
                                                    MyFilledTonalButton(
                                                        text = url.name,
                                                        enabled = true,
                                                        shape = RoundedCornerShape(8.dp),
                                                        onClickAction = {
                                                            navController.navigate(url.url)
                                                        },
                                                        buttonColor = when (url.tipo) {
                                                            "info" -> MaterialTheme.colorScheme.primaryContainer
                                                            "success" -> MaterialTheme.colorScheme.onPrimaryContainer
                                                            "danger" -> MaterialTheme.colorScheme.errorContainer
                                                            "warning" -> MaterialTheme.colorScheme.tertiaryContainer
                                                            else -> MaterialTheme.colorScheme.surfaceContainerLowest
                                                        },
                                                        textColor = when (url.tipo) {
                                                            "info" -> MaterialTheme.colorScheme.primary
                                                            "success" -> MaterialTheme.colorScheme.onPrimary
                                                            "danger" -> MaterialTheme.colorScheme.error
                                                            "warning" -> MaterialTheme.colorScheme.tertiary
                                                            else -> MaterialTheme.colorScheme.onSurface
                                                        },
                                                    )
                                                    Spacer(Modifier.width(4.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun PeriodoItem(
    periodo: Periodo,
    homeViewModel: HomeViewModel
) {
    val periodoSelect by homeViewModel.periodoSelect.collectAsState(null)
    val data by homeViewModel.homeData.collectAsState(null)
    data?.let {

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(
                    color = if (periodoSelect!!.id == periodo.id)
                        MaterialTheme.colorScheme.secondaryContainer
                    else Color.Transparent
                ),
            onClick = {
                homeViewModel.changePeriodoSelect(periodo)
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                text = periodo.nombre,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = if (periodoSelect!!.id == periodo.id)
                    MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.outline
            )
        }
    }
}
