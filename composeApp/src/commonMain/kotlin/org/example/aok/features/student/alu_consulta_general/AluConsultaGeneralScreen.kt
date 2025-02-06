package org.example.aok.features.student.alu_consulta_general

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.example.aok.core.logInfo
import org.example.aok.data.network.alu_consulta_general.ConsultaGeneralAlumno
import org.example.aok.data.network.alu_consulta_general.ConsultaGeneralMatricula
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluConsultaGeneralScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluConsultaGeneralViewModel: AluConsultaGeneralViewModel
) {
    DashBoardScreen(
        title = "Consulta General",
        navController = navController,
        content = {
            Screen(
                navController,
                homeViewModel,
                aluConsultaGeneralViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    aluConsultaGeneralViewModel: AluConsultaGeneralViewModel
) {
    val data by aluConsultaGeneralViewModel.data.collectAsState(null)
    val error by homeViewModel.error.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    logInfo("prueba", "data: ${homeViewModel.homeData.value}")

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluConsultaGeneralViewModel.onloadAluConsultaGeneral(
                it, homeViewModel)
        }
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item { data?.let { dataAlumno(it.alumno) } }
            item { data?.let { it.matricula?.let { it1 -> dataMatricula(it1) } } }
        }
    }
}

@Composable
fun dataAlumno(
    data: ConsultaGeneralAlumno
) {
    Text(
        text = "Datos del alumno",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}


@Composable
fun dataMatricula(
    data: ConsultaGeneralMatricula
) {
    Text(
        text = "Datos de matrícula",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}