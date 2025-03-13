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
import org.example.aok.features.common.docBiblioteca.DocBibliotecaScreen
import org.example.aok.features.common.docBiblioteca.DocBibliotecaViewModel
import org.example.aok.features.common.extra.CabScreen
import org.example.aok.features.common.home.HomeScreen
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginScreen
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.features.common.reportes.ReportesScreen
import org.example.aok.features.common.reportes.ReportesViewModel
import org.example.aok.features.student.alu_ayuda_financiera.AluAyudaFinancieraScreen
import org.example.aok.features.student.alu_consulta_general.AluConsultaGeneralScreen
import org.example.aok.features.student.alu_consulta_general.AluConsultaGeneralViewModel
import org.example.aok.features.student.alu_cronograma.AluCronogramaScreen
import org.example.aok.features.student.alu_cronograma.AluCronogramaViewModel
import org.example.aok.features.student.alu_documentos.AluDocumentosScreen
import org.example.aok.features.student.alu_documentos.AluDocumentosViewModel
import org.example.aok.features.student.alu_finanzas.AluFinannzasScreen
import org.example.aok.features.student.alu_finanzas.AluFinanzasViewModel
import org.example.aok.features.student.alu_horario.AluHorarioScreen
import org.example.aok.features.student.alu_horario.AluHorarioViewModel
import org.example.aok.features.student.alu_malla.AluMallaScreen
import org.example.aok.features.student.alu_malla.AluMallaViewModel
import org.example.aok.features.student.alu_materias.AluMateriasScreen
import org.example.aok.features.student.alu_materias.AluMateriasViewModel
import org.example.aok.features.student.alu_matricula.AluMatriculaScreen
import org.example.aok.features.student.alu_matricula.AluMatriculaViewModel
import org.example.aok.features.student.alu_notas.AluNotasScreen
import org.example.aok.features.student.alu_notas.AluNotasViewModel
import org.example.aok.features.student.alu_solicitud_beca.AluSolicitudBecaScreen
import org.example.aok.features.student.alu_solicitud_beca.AluSolicitudBecaViewModel
import org.example.aok.features.student.alu_solicitudes_online.AddSolicitudForm
import org.example.aok.features.student.alu_solicitudes_online.AluSolicitudesScreen
import org.example.aok.features.student.alu_solicitudes_online.AluSolicitudesViewModel
import org.example.aok.features.student.facturacion_electronica.AluFacturacionScreen
import org.example.aok.features.student.facturacion_electronica.AluFacturacionViewModel
import org.example.aok.features.student.pago_online.PagoOnlineScreen
import org.example.aok.features.student.pago_online.PagoOnlineViewModel
import org.example.aok.features.teacher.pro_clases.ProClasesScreen
import org.example.aok.features.teacher.pro_clases.ProClasesViewModel
import org.example.aok.features.teacher.pro_evaluaciones.ProCalificacionesScreen
import org.example.aok.features.teacher.pro_evaluaciones.ProEvaluacionesScreen
import org.example.aok.features.teacher.pro_evaluaciones.ProEvaluacionesViewModel
import org.example.aok.features.teacher.pro_horarios.ProHorariosScreen
import org.example.aok.features.teacher.pro_horarios.ProHorariosViewModel

@Composable
fun MyNavigation(
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
    proClasesViewModel: ProClasesViewModel,
    proHorariosViewModel: ProHorariosViewModel,
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    aluDocumentosViewModel: AluDocumentosViewModel,
    docBibliotecaViewModel: DocBibliotecaViewModel,
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel,
    aluConsultaGeneralViewModel: AluConsultaGeneralViewModel,
    aluMatriculaViewModel: AluMatriculaViewModel,
    reportesViewModel: ReportesViewModel,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("404") {NotFoundScreen(onNavigateBack = { navController.popBackStack() }) }
        composable("login") { LoginScreen(navController, loginViewModel, homeViewModel) }
        composable("home") { HomeScreen(navController, homeViewModel, loginViewModel) }
        composable("inscripciones") { InscripcionesScreen(navController, homeViewModel, loginViewModel, inscripcionesViewModel) }
        composable("account") { AccountScreen(navController, homeViewModel, loginViewModel, accountViewModel) }
        composable("alu_finanzas") { AluFinannzasScreen(navController, homeViewModel, loginViewModel, aluFinanzasViewModel) }
        composable("alu_cronograma") { AluCronogramaScreen(navController, homeViewModel, loginViewModel, aluCronogramaViewModel) }
        composable("alu_malla") { AluMallaScreen(navController, homeViewModel, loginViewModel, aluMallaViewModel) }
        composable("alu_horarios") { AluHorarioScreen(navController, homeViewModel, loginViewModel, aluHorarioViewModel) }
        composable("online") { PagoOnlineScreen(navController, homeViewModel, loginViewModel, pagoOnlineViewModel) }
        composable("alu_materias") { AluMateriasScreen(navController, homeViewModel, loginViewModel, aluMateriasViewModel) }
        composable("alu_facturacion_electronica") { AluFacturacionScreen(navController, homeViewModel, loginViewModel, aluFacturacionViewModel) }
        composable("alu_notas") { AluNotasScreen(navController, homeViewModel, loginViewModel, aluNotasViewModel) }
        composable("docentes") { DocentesScreen(navController, homeViewModel, loginViewModel, docentesViewModel) }
        composable("pro_clases") { ProClasesScreen(navController, homeViewModel, loginViewModel, proClasesViewModel) }
        composable("pro_horarios") { ProHorariosScreen(navController, homeViewModel, loginViewModel, proHorariosViewModel) }
        composable("solicitudonline") { AluSolicitudesScreen(navController, homeViewModel, loginViewModel, aluSolicitudesViewModel) }
        composable("addSolicitud") { AddSolicitudForm(aluSolicitudesViewModel, homeViewModel, navController) }
        composable("admin_ayudafinanciera") { AluAyudaFinancieraScreen(navController, homeViewModel, loginViewModel) }
        composable("alumnos_cab") { CabScreen(navController, homeViewModel, loginViewModel) }
        composable("documentos_alu") { AluDocumentosScreen(navController, homeViewModel, loginViewModel, aluDocumentosViewModel) }
        composable("documentos") { DocBibliotecaScreen(navController, homeViewModel, loginViewModel, docBibliotecaViewModel) }
        composable("beca_solicitud") { AluSolicitudBecaScreen(navController, homeViewModel, loginViewModel, aluSolicitudBecaViewModel) }
        composable("consultaalumno") { AluConsultaGeneralScreen(navController, homeViewModel, loginViewModel, aluConsultaGeneralViewModel) }
        composable("alu_matricula") { AluMatriculaScreen(navController, homeViewModel, loginViewModel, aluMatriculaViewModel) }
        composable("reportes") { ReportesScreen(navController, homeViewModel, loginViewModel, reportesViewModel) }
        composable("pro_evaluaciones") { ProEvaluacionesScreen(navController, homeViewModel, loginViewModel, proEvaluacionesViewModel) }
        composable("pro_calificaciones") { ProCalificacionesScreen(navController, homeViewModel, proEvaluacionesViewModel) }

    }
}