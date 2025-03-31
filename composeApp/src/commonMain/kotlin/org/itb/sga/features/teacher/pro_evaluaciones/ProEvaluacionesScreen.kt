package org.itb.sga.features.teacher.pro_evaluaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.ModelsViewModel
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluacionesMateria
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun ProEvaluacionesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    modelsViewModel: ModelsViewModel
) {
    DashBoardScreen(
        title = "Calificaciones",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                proEvaluacionesViewModel,
                navController,
                modelsViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    navController: NavHostController,
    modelsViewModel: ModelsViewModel
) {
    val data by proEvaluacionesViewModel.data.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val error by proEvaluacionesViewModel.error.collectAsState(null)
    val periodo by homeViewModel.periodoSelect.collectAsState(null)

    LaunchedEffect(periodo) {
        homeViewModel.homeData.value!!.persona.idDocente?.let {
            proEvaluacionesViewModel.onloadProEvaluaciones(
                id = it,
                homeViewModel = homeViewModel,
            )
        }
    }

    val dataFilter = if (query.isNotEmpty()) {
        data?.materias?.filter { item ->
            item.nombre.contains(query, ignoreCase = true)
        }
    } else {
        data?.materias
    } ?: emptyList()

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        periodo?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    text = it.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))

            when {
                data?.materias?.isEmpty() == true -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No se encontraron materias para el periodo seleccionado.",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    Column (
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                        ) {
                            items(dataFilter) { materia ->
                                MateriaItem(materia, proEvaluacionesViewModel, navController, modelsViewModel)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

        } ?: run {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Seleccione un periodo.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }
    }

    error?.let {
        MyErrorAlert(
            titulo = it.title,
            mensaje = it.error,
            onDismiss = {
                proEvaluacionesViewModel.clearError()
            },
            showAlert = true
        )
    }
}

@Composable
fun MateriaItem(
    materia: ProEvaluacionesMateria,
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    navController: NavHostController,
    modelsViewModel: ModelsViewModel
) {
    MyCard (
        onClick = {
            proEvaluacionesViewModel.updateMateriaSelect(materia)
            navController.navigate("pro_calificaciones")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = materia.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${materia.grupo} (${materia.nivelMalla})",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${materia.desde} - ${materia.hasta}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


