package org.example.aok.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.aok.features.admin.docentes.DocentesScreen
import org.example.aok.features.admin.docentes.DocentesViewModel
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
import org.example.aok.features.student.alu_horario.AluHorarioScreen
import org.example.aok.features.student.alu_horario.AluHorarioViewModel
import org.example.aok.features.student.alu_malla.AluMallaScreen
import org.example.aok.features.student.alu_malla.AluMallaViewModel
import org.example.aok.features.student.alu_materias.AluMateriasScreen
import org.example.aok.features.student.alu_materias.AluMateriasViewModel
import org.example.aok.features.student.alu_notas.AluNotasScreen
import org.example.aok.features.student.alu_notas.AluNotasViewModel
import org.example.aok.features.student.facturacion_electronica.AluFacturacionScreen
import org.example.aok.features.student.facturacion_electronica.AluFacturacionViewModel
import org.example.aok.features.student.pago_online.PagoOnlineScreen
import org.example.aok.features.student.pago_online.PagoOnlineViewModel
import org.example.aok.features.teacher.pro_clases.ProClasesScreen
import org.example.aok.features.teacher.pro_clases.ProClasesViewModel

@Composable
fun MyNavigation(
    mainViewModel: MainViewModel,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    inscripcionesViewModel: InscripcionesViewModel,
    accountViewModel: AccountViewModel,
    aluFinanzasViewModel: AluFinanzasViewModel,
    aluCronogramaViewModel: AluCronogramaViewModel,
    aluMallaViewModel: AluMallaViewModel,
    aluHorarioViewModel: AluHorarioViewModel,
    pagoOnlineViewModel: PagoOnlineViewModel,
    aluMateriasViewModel: AluMateriasViewModel,
    aluFacturacionViewModel: AluFacturacionViewModel,
    aluNotasViewModel: AluNotasViewModel,
    docentesViewModel: DocentesViewModel,
    proClasesViewModel: ProClasesViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") { LoginScreen(navController, loginViewModel, homeViewModel) }
        composable("home") { HomeScreen(navController, mainViewModel, homeViewModel, loginViewModel) }
        composable("inscripciones") { InscripcionesScreen(navController, mainViewModel, homeViewModel, loginViewModel, inscripcionesViewModel) }
        composable("account") { AccountScreen(navController, mainViewModel, homeViewModel, loginViewModel, accountViewModel) }
        composable("alu_finanzas") { AluFinannzasScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluFinanzasViewModel) }
        composable("alu_cronograma") { AluCronogramaScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluCronogramaViewModel) }
        composable("alu_malla") { AluMallaScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluMallaViewModel) }
        composable("alu_horarios") { AluHorarioScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluHorarioViewModel) }
        composable("online") { PagoOnlineScreen(navController, mainViewModel, homeViewModel, loginViewModel, pagoOnlineViewModel) }
        composable("alu_materias") { AluMateriasScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluMateriasViewModel) }
        composable("alu_facturacion_electronica") { AluFacturacionScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluFacturacionViewModel) }
        composable("alu_notas") { AluNotasScreen(navController, mainViewModel, homeViewModel, loginViewModel, aluNotasViewModel) }
        composable("docentes") { DocentesScreen(navController, mainViewModel, homeViewModel, loginViewModel, docentesViewModel) }
        composable("pro_clases") { ProClasesScreen(navController, mainViewModel, homeViewModel, loginViewModel, proClasesViewModel) }
    }
}