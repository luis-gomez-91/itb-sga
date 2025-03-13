package org.example.aok.features.common.reportes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.data.network.reportes.Reporte
import org.example.aok.data.network.reportes.ReporteCategoria
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.alerts.MyErrorAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun ReportesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    reportesViewModel: ReportesViewModel
) {
    DashBoardScreen(
        title = "Reportes PDF",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                reportesViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    reportesViewModel: ReportesViewModel
) {
    val data by reportesViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val error by reportesViewModel.error.collectAsState(null)

    LaunchedEffect(Unit) {
        homeViewModel.homeData.value!!.persona.idPersona.let {
            reportesViewModel.onloadReportes(
                id = it,
                homeViewModel = homeViewModel,
            )
        }
    }

    val dataFilter = if (query.isNotEmpty()) {
        data.filter { item ->
            item.reportes.any { report ->
                report.descripcion.contains(query, ignoreCase = true)
            }
        }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()

        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(dataFilter) { categoria ->
                    CategoriaItem(categoria, reportesViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        error?.let {
            MyErrorAlert(
                titulo = it.title,
                mensaje = it.error,
                onDismiss = {
                    homeViewModel.clearError()
                },
                showAlert = true
            )
        }
    }
}

@Composable
fun CategoriaItem(
    categoria: ReporteCategoria,
    reportesViewModel: ReportesViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard (
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = categoria.categoria,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.width(8.dp))
                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .let {
                            if (categoria.reportes.size > 10) {
                                it.height(300.dp).verticalScroll(rememberScrollState()) // Desplazable si hay muchos elementos
                            } else {
                                it // Se ajusta automáticamente si hay pocos elementos
                            }
                        }
                ) {
                    categoria.reportes.forEach { reporte ->
                        ReporteItem(reporte)
                    }
                }
            }

        }

    }
}

@Composable
fun ReporteItem(
    reporte: Reporte
) {
    HorizontalDivider()
    Spacer(Modifier.height(4.dp))
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.PictureAsPdf,
            contentDescription = "Report",
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = reporte.descripcion,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(Modifier.height(4.dp))
}
