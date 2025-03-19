package org.itb.sga.features.student.alu_solicitud_beca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.features.student.alu_solicitud_beca.ficha_socioeconomica.FichaSocioeconomicaScreen
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.dashboard.DashBoardScreen
import org.itb.sga.ui.components.dashboard.DashboardScreen2

@Composable
fun AluSolicitudBecaScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel
) {
    val screenSelect by homeViewModel.screenSelect.collectAsState("screen")
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluSolicitudBecaViewModel.onloadAluSolicitudBeca(
                it, homeViewModel
            )
        }
    }


    val mapaScreenSelect = mapOf<String, @Composable () -> Unit>(
        "screen" to {
            DashBoardScreen(
                title = "Solicitud beca",
                navController = navController,
                content = {
                    Screen(
                        homeViewModel,
                        navController,
                        aluSolicitudBecaViewModel
                    )
                },
                homeViewModel = homeViewModel,
                loginViewModel = loginViewModel
            )
        },
        "ficha" to {
            DashboardScreen2(
                content = { FichaSocioeconomicaScreen(aluSolicitudBecaViewModel, homeViewModel, scope) },
                backScreen = "beca_solicitud",
                title = "Ficha socioeconómica",
                navHostController = navController
            )
        }
    )

    mapaScreenSelect[screenSelect]?.invoke() ?: run {
        navController.navigate("404")
    }
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel
) {

    val showRequisitos by aluSolicitudBecaViewModel.showRequisitos.collectAsState(false)

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MyFilledTonalButton(
            text = "Requisitos para Beca",
            enabled = true,
            onClickAction = {
                aluSolicitudBecaViewModel.changeShowRequisitos(true)
            },
            buttonColor = MaterialTheme.colorScheme.secondaryContainer,
            textColor = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(16.dp))

        MyFilledTonalButton(
            text = "Ficha Socioeconómica DOBE",
            enabled = true,
            onClickAction = {
                homeViewModel.changeScreenSelect("ficha")
            },
            buttonColor = MaterialTheme.colorScheme.secondaryContainer,
            textColor = MaterialTheme.colorScheme.secondary
        )
    }

    if (showRequisitos) {
        RequisitosBeca(true, { aluSolicitudBecaViewModel.changeShowRequisitos(false) }, homeViewModel)
    }
}