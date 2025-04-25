package org.itb.sga

import AppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import aok.composeapp.generated.resources.Res
import aok.composeapp.generated.resources.logo
import aok.composeapp.generated.resources.logo_dark
import org.itb.sga.core.MyNavigation
import org.itb.sga.core.appIsLastVersion
import org.itb.sga.core.openPlayStoreOrAppStore
import org.itb.sga.features.admin.docentes.DocentesViewModel
import org.itb.sga.features.admin.inscripciones.InscripcionesViewModel
import org.itb.sga.features.common.account.AccountViewModel
import org.itb.sga.features.common.docBiblioteca.DocBibliotecaViewModel
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.features.common.reportes.ReportesViewModel
import org.itb.sga.features.student.alu_consulta_general.AluConsultaGeneralViewModel
import org.itb.sga.features.student.alu_cronograma.AluCronogramaViewModel
import org.itb.sga.features.student.alu_documentos.AluDocumentosViewModel
import org.itb.sga.features.student.alu_finanzas.AluFinanzasViewModel
import org.itb.sga.features.student.alu_horario.AluHorarioViewModel
import org.itb.sga.features.student.alu_malla.AluMallaViewModel
import org.itb.sga.features.student.alu_materias.AluMateriasViewModel
import org.itb.sga.features.student.alu_matricula.AluMatriculaViewModel
import org.itb.sga.features.student.alu_notas.AluNotasViewModel
import org.itb.sga.features.student.alu_solicitud_beca.AluSolicitudBecaViewModel
import org.itb.sga.features.student.alu_solicitudes_online.AluSolicitudesViewModel
import org.itb.sga.features.student.facturacion_electronica.AluFacturacionViewModel
import org.itb.sga.features.student.pago_online.PagoOnlineViewModel
import org.itb.sga.features.teacher.pro_clases.ProClasesViewModel
import org.itb.sga.features.teacher.pro_cronograma.ProCronogramaViewModel
import org.itb.sga.features.teacher.pro_entrega_actas.ProEntregaActasViewModel
import org.itb.sga.features.teacher.pro_evaluaciones.ProEvaluacionesViewModel
import org.itb.sga.features.teacher.pro_horarios.ProHorariosViewModel
import org.itb.sga.ui.components.MyFilledTonalButton
import org.jetbrains.compose.resources.painterResource

@Composable
fun App(
    homeViewModel : HomeViewModel,
    loginViewModel: LoginViewModel
) {
    val accountViewModel = remember { AccountViewModel() }
    val inscripcionesViewModel = remember { InscripcionesViewModel() }
    val aluFinanzasViewModel = remember { AluFinanzasViewModel() }
    val aluCronogramaViewModel = remember { AluCronogramaViewModel() }
    val aluMallaViewModel = remember { AluMallaViewModel() }
    val aluHorarioViewModel = remember { AluHorarioViewModel() }
    val pagoOnlineViewModel = remember { PagoOnlineViewModel() }
    val aluMateriasViewModel = remember { AluMateriasViewModel() }
    val aluFacturacionViewModel = remember { AluFacturacionViewModel() }
    val aluNotasViewModel = remember { AluNotasViewModel() }
    val aluSolicitudesViewModel = remember { AluSolicitudesViewModel() }
    val docentesViewModel = remember { DocentesViewModel() }
    val proClasesViewModel = remember { ProClasesViewModel() }
    val proHorariosViewModel = remember { ProHorariosViewModel() }
    val aluDocumentosViewModel = remember { AluDocumentosViewModel() }
    val docBibliotecaViewModel = remember { DocBibliotecaViewModel() }
    val aluSolicitudBecaViewModel = remember { AluSolicitudBecaViewModel() }
    val aluConsultaGeneralViewModel = remember { AluConsultaGeneralViewModel() }
    val aluMatriculaViewModel = remember { AluMatriculaViewModel() }
    val reportesViewModel = remember { ReportesViewModel() }
    val proEvaluacionesViewModel = remember { ProEvaluacionesViewModel() }
    val proCronogramaViewModel = remember { ProCronogramaViewModel() }
    val proEntregaActasViewModel = remember { ProEntregaActasViewModel() }

//    Generar el SHA-256 en terminal para deep links
//    keytool -list -v -keystore C:\Users\lagomez\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android

//    PS C:\Users\lagomez\AndroidStudioProjects\app-ITB-KMM2> keytool -list -v -keystore C:\Users\lagomez\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
//    Alias name: androiddebugkey
//            Creation date: 6 ene. 2025
//    Entry type: PrivateKeyEntry
//            Certificate chain length: 1
//    Certificate[1]:
//    Owner: C=US, O=Android, CN=Android Debug
//    Issuer: C=US, O=Android, CN=Android Debug
//    Serial number: 1
//    Valid from: Mon Jan 06 11:15:03 ECT 2025 until: Wed Dec 30 11:15:03 ECT 2054
//    Certificate fingerprints:
//    SHA1: 5A:20:E1:48:36:29:8F:BB:34:1B:92:98:D0:C3:51:74:7C:20:85:F4
//    SHA256: C9:B7:18:E4:BF:43:F8:5C:D8:2C:DD:91:CF:57:D3:5F:89:90:E9:04:A6:EC:32:4E:05:0E:18:8E:CE:14:18:9E
//    Signature algorithm name: SHA256withRSA
//    Subject Public Key Algorithm: 2048-bit RSA key
//    Version: 1



    AppTheme(
        homeViewModel = homeViewModel
    ) {
        val appLastVersion by loginViewModel.appLastVersion.collectAsState(null)

        LaunchedEffect(Unit) {
            loginViewModel.fetchLastVersionApp()
        }

        appLastVersion?.let {
            if (appIsLastVersion(it)) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNavigation(
                        loginViewModel = loginViewModel,
                        homeViewModel = homeViewModel,
                        inscripcionesViewModel = inscripcionesViewModel,
                        accountViewModel = accountViewModel,
                        aluFinanzasViewModel = aluFinanzasViewModel,
                        aluCronogramaViewModel = aluCronogramaViewModel,
                        aluMallaViewModel = aluMallaViewModel,
                        aluHorarioViewModel = aluHorarioViewModel,
                        pagoOnlineViewModel = pagoOnlineViewModel,
                        aluMateriasViewModel = aluMateriasViewModel,
                        aluFacturacionViewModel = aluFacturacionViewModel,
                        aluNotasViewModel = aluNotasViewModel,
                        docentesViewModel = docentesViewModel,
                        proClasesViewModel = proClasesViewModel,
                        proHorariosViewModel = proHorariosViewModel,
                        aluSolicitudesViewModel = aluSolicitudesViewModel,
                        aluDocumentosViewModel = aluDocumentosViewModel,
                        docBibliotecaViewModel = docBibliotecaViewModel,
                        aluSolicitudBecaViewModel = aluSolicitudBecaViewModel,
                        aluConsultaGeneralViewModel = aluConsultaGeneralViewModel,
                        aluMatriculaViewModel = aluMatriculaViewModel,
                        reportesViewModel = reportesViewModel,
                        proEvaluacionesViewModel = proEvaluacionesViewModel,
                        proCronogramaViewModel = proCronogramaViewModel,
                        proEntregaActasViewModel = proEntregaActasViewModel
                    )
                }
            } else {
                val selectedTheme by homeViewModel.selectedTheme.collectAsState(null)
                var imageLogo by remember { mutableStateOf(Res.drawable.logo) }

                LaunchedEffect(selectedTheme) {
                    imageLogo = if (selectedTheme?.dark == true) {
                        Res.drawable.logo_dark
                    } else {
                        Res.drawable.logo
                    }
                }

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp, start = 32.dp, end = 32.dp, bottom = 64.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(imageLogo),
                        contentDescription = "logo",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "¡Una nueva versión está disponible! Actualiza la app para seguir disfrutando de las mejores funciones.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))
                    MyFilledTonalButton(
                        text = "Actualizar",
                        buttonColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.primary,
                        icon = Icons.Filled.Update,
                        onClickAction = { openPlayStoreOrAppStore() },
                        iconSize = 32.dp,
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}