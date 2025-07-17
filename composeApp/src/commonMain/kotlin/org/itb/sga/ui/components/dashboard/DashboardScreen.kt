package org.itb.sga.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.itb.sga.ui.components.ConnectivityWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    title: String,
    navController: NavHostController,
    content: @Composable () -> Unit = {},
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {

    val internetConnected by homeViewModel.konnectivity.isConnectedState.collectAsState()
    ConnectivityWrapper(
        internetConnected,
        homeViewModel
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val showBottomSheet by homeViewModel.showBottomSheet.collectAsState(false)

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
                Notificacion(homeViewModel, navController)
            }
        )
    }


}

@Composable
fun PeriodoItem(
    periodo: Periodo,
    homeViewModel: HomeViewModel
) {
    val periodoSelect by homeViewModel.periodoSelect.collectAsState(null)
    periodoSelect?.let { select ->
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(
                    color = if (select.id == periodo.id)
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
                color = if (select.id == periodo.id)
                    MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.outline
            )
        }
    }
}
