package org.example.aok.features.teacher.pro_evaluaciones

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.logInfo
import org.example.aok.data.network.pro_evaluaciones.ProEvaluacionesMateria
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.alerts.MyErrorAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun ProEvaluacionesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    DashBoardScreen(
        title = "Calificaciones",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                proEvaluacionesViewModel,
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
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    navController: NavHostController
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
                    MateriaItem(materia, proEvaluacionesViewModel, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        error?.let {
            logInfo("evaluaciones", "${error}")
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
}

@Composable
fun MateriaItem(
    materia: ProEvaluacionesMateria,
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    navController: NavHostController
) {
    MyCard (
        onClick = {
            proEvaluacionesViewModel.updateMateriaSelect(materia)
            navController.navigate("pro_calificaciones")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = materia.nombre,
                style = MaterialTheme.typography.labelMedium,
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


