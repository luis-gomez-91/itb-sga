package org.example.aok.features.student.facturacion_electronica

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.data.network.AluFacturacion
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluFacturacionScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
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
                aluFacturacionViewModel
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluFacturacionViewModel: AluFacturacionViewModel
) {
    val data by aluFacturacionViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")

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
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
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

            Text(
                text = data.numeroAutorizacion,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )


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
//                        homeViewModel.openPDF("https://sga.itb.edu.ec/media//documentos/userreports/lagomez11/factura_sri20241001_130714.pdf")
                        homeViewModel.openURL("https://sga.itb.edu.ec/${data.xml}")
                    },
                    buttonColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimary
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
                    textColor = MaterialTheme.colorScheme.tertiary
                )
            }

        }


    }
}