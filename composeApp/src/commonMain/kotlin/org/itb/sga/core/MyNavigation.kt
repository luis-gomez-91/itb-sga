package org.itb.sga.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.itb.sga.features.admin.docentes.DocentesScreen
import org.itb.sga.features.admin.docentes.DocentesViewModel
import org.itb.sga.features.admin.inscripciones.InscripcionesScreen
import org.itb.sga.features.admin.inscripciones.InscripcionesViewModel
import org.itb.sga.features.common.account.AccountEditInfoScreen
import org.itb.sga.features.common.account.AccountScreen
import org.itb.sga.features.common.account.AccountViewModel
import org.itb.sga.features.common.docBiblioteca.DocBibliotecaScreen
import org.itb.sga.features.common.docBiblioteca.DocBibliotecaViewModel
import org.itb.sga.features.common.extra.CabScreen
import org.itb.sga.features.common.home.HomeScreen
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginScreen
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.features.common.reportes.ReportesScreen
import org.itb.sga.features.common.reportes.ReportesViewModel
import org.itb.sga.features.student.alu_ayuda_financiera.AluAyudaFinancieraScreen
import org.itb.sga.features.student.alu_consulta_general.AluConsultaGeneralScreen
import org.itb.sga.features.student.alu_consulta_general.AluConsultaGeneralViewModel
import org.itb.sga.features.student.alu_cronograma.AluCronogramaScreen
import org.itb.sga.features.student.alu_cronograma.AluCronogramaViewModel
import org.itb.sga.features.student.alu_documentos.AluDocumentosScreen
import org.itb.sga.features.student.alu_documentos.AluDocumentosViewModel
import org.itb.sga.features.student.alu_finanzas.AluFinannzasScreen
import org.itb.sga.features.student.alu_finanzas.AluFinanzasViewModel
import org.itb.sga.features.student.alu_horario.AluHorarioScreen
import org.itb.sga.features.student.alu_horario.AluHorarioViewModel
import org.itb.sga.features.student.alu_malla.AluMallaScreen
import org.itb.sga.features.student.alu_malla.AluMallaViewModel
import org.itb.sga.features.student.alu_materias.AluMateriasScreen
import org.itb.sga.features.student.alu_materias.AluMateriasViewModel
import org.itb.sga.features.student.alu_matricula.AluMatriculaScreen
import org.itb.sga.features.student.alu_matricula.AluMatriculaViewModel
import org.itb.sga.features.student.alu_notas.AluNotasScreen
import org.itb.sga.features.student.alu_notas.AluNotasViewModel
import org.itb.sga.features.student.alu_solicitud_beca.AluSolicitudBecaScreen
import org.itb.sga.features.student.alu_solicitud_beca.AluSolicitudBecaViewModel
import org.itb.sga.features.student.alu_solicitudes_online.AddSolicitudForm
import org.itb.sga.features.student.alu_solicitudes_online.AluSolicitudesScreen
import org.itb.sga.features.student.alu_solicitudes_online.AluSolicitudesViewModel
import org.itb.sga.features.student.facturacion_electronica.AluFacturacionScreen
import org.itb.sga.features.student.facturacion_electronica.AluFacturacionViewModel
import org.itb.sga.features.student.pago_online.PagoOnlineScreen
import org.itb.sga.features.student.pago_online.PagoOnlineViewModel
import org.itb.sga.features.teacher.pro_clases.ProClasesScreen
import org.itb.sga.features.teacher.pro_clases.ProClasesViewModel
import org.itb.sga.features.teacher.pro_clases.VerClaseScreen
import org.itb.sga.features.teacher.pro_cronograma.ProCronogramaScreen
import org.itb.sga.features.teacher.pro_cronograma.ProCronogramaViewModel
import org.itb.sga.features.teacher.pro_entrega_actas.ProEntregaActasScreen
import org.itb.sga.features.teacher.pro_entrega_actas.ProEntregaActasViewModel
import org.itb.sga.features.teacher.pro_evaluaciones.ProCalificacionesScreen
import org.itb.sga.features.teacher.pro_evaluaciones.ProEvaluacionesScreen
import org.itb.sga.features.teacher.pro_evaluaciones.ProEvaluacionesViewModel
import org.itb.sga.features.teacher.pro_horarios.ProHorariosScreen
import org.itb.sga.features.teacher.pro_horarios.ProHorariosViewModel
import org.itb.sga.ui.components.NotFoundScreen

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
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    proCronogramaViewModel: ProCronogramaViewModel,
    proEntregaActasViewModel: ProEntregaActasViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("404") { NotFoundScreen(onNavigateBack = { navController.popBackStack() }) }
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
        composable("pro_horarios") { ProHorariosScreen(navController, homeViewModel, loginViewModel, proHorariosViewModel, proClasesViewModel) }
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
        composable("pro_cronograma") { ProCronogramaScreen(navController, homeViewModel, loginViewModel, proCronogramaViewModel) }
        composable("pro_entrega_acta") { ProEntregaActasScreen(navController, homeViewModel, loginViewModel, proEntregaActasViewModel) }


        composable("pro_calificaciones") { ProCalificacionesScreen(navController, homeViewModel, proEvaluacionesViewModel) }
        composable("ver_clase") { VerClaseScreen(navController, proClasesViewModel) }
        composable("account_edit") { AccountEditInfoScreen(navController, accountViewModel, reportesViewModel) }

    }
}