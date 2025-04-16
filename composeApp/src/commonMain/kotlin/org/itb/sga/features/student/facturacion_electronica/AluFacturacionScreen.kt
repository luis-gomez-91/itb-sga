package org.itb.sga.features.student.facturacion_electronica

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.data.network.AluFacturacion
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluFacturacionScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluFacturacionViewModel: AluFacturacionViewModel
) {
    DashBoardScreen(
        title = "Facturación electrónica",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluFacturacionViewModel,
                navController
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluFacturacionViewModel: AluFacturacionViewModel,
    navController: NavHostController
) {
    val data by aluFacturacionViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val error by homeViewModel.error.collectAsState(null)

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluFacturacionViewModel.onloadAluFacturacion(
                it,
                homeViewModel
            )
        }
    }

    val dataFiltada = if (searchQuery.isNotEmpty()) {
        data.filter { it.numero.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(dataFiltada) { facturacion ->
                    CardItem(
                        data = facturacion,
                        aluFacturacionViewModel = aluFacturacionViewModel,
                        homeViewModel = homeViewModel
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
fun CardItem(
    data: AluFacturacion,
    aluFacturacionViewModel: AluFacturacionViewModel,
    homeViewModel: HomeViewModel
) {
    MyCard (
        modifier = Modifier.padding(bottom = 4.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = data.numero,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyAssistChip(
                    label = data.tipo,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    labelColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.Filled.Receipt
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = data.fecha,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Filled.DateRange
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = data.numeroAutorizacion,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                MyFilledTonalButton(
                    text = "XML",
                    enabled = true,
                    icon = Icons.Filled.Download,
                    onClickAction = {
                        homeViewModel.openURL("https://sga.itb.edu.ec/${data.xml}")
                    },
                    buttonColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    iconSize = 16.dp,
                    textStyle = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.width(4.dp))

                MyFilledTonalButton(
                    text = "RIDE",
                    enabled = true,
                    icon = Icons.Filled.Download,
                    onClickAction = {
                        aluFacturacionViewModel.downloadRIDE("factura_sri", data.id, homeViewModel)
                    },
                    buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.tertiary,
                    iconSize = 16.dp,
                    textStyle = MaterialTheme.typography.labelSmall
                )
            }

        }


    }
}