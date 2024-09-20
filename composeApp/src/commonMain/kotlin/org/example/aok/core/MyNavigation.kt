package org.example.aok.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.aok.features.admin.inscripciones.InscripcionesScreen
import org.example.aok.features.admin.inscripciones.InscripcionesViewModel
import org.example.aok.features.common.account.AccountScreen
import org.example.aok.features.common.account.AccountViewModel
import org.example.aok.features.common.home.HomeScreen
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginScreen
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.features.student.alu_cronograma.AluCronogramaScreen
import org.example.aok.features.student.alu_cronograma.AluCronogramaViewModel
import org.example.aok.features.student.alu_finanzas.AluFinannzasScreen
import org.example.aok.features.student.alu_finanzas.AluFinanzasViewModel
import org.example.aok.features.student.alu_malla.AluMallaScreen
import org.example.aok.features.student.alu_malla.AluMallaViewModel

@Composable
fun MyNavigation(
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    inscripcionesViewModel: InscripcionesViewModel,
    accountViewModel: AccountViewModel,
    aluFinanzasViewModel: AluFinanzasViewModel,
    aluCronogramaViewModel: AluCronogramaViewModel,
    aluMallaViewModel: AluMallaViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController, loginViewModel) }
        composable("home") { HomeScreen(navController, mainViewModel, homeViewModel, loginViewModel) }
        composable("inscripciones") { InscripcionesScreen(navController, mainViewModel, homeViewModel, loginViewModel, inscripcionesViewModel) }
        composable("account") { AccountScreen(navController, mainViewModel, homeViewModel, loginViewModel, accountViewModel) }
        composable("alu_finanzas") { AluFinannzasScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluFinanzasViewModel) }
        composable("alu_cronograma") { AluCronogramaScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluCronogramaViewModel) }
        composable("alu_malla") { AluMallaScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluMallaViewModel) }
    }
}