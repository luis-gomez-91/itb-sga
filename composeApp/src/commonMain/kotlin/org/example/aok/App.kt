package org.example.aok

import AppTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import org.example.aok.core.MainViewModel
import org.example.aok.core.MyNavigation
import org.example.aok.features.admin.inscripciones.InscripcionesViewModel
import org.example.aok.features.common.account.AccountViewModel
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.features.student.alu_cronograma.AluCronogramaViewModel
import org.example.aok.features.student.alu_finanzas.AluFinanzasViewModel
import org.example.aok.features.student.alu_horario.AluHorarioViewModel
import org.example.aok.features.student.alu_malla.AluMallaViewModel
import org.example.aok.features.student.alu_materias.AluMateriasViewModel
import org.example.aok.features.student.facturacion_electronica.AluFacturacionViewModel
import org.example.aok.features.student.pago_online.PagoOnlineViewModel

@Composable
fun App() {
    val mainViewModel = remember { MainViewModel() }
    val loginViewModel = remember { LoginViewModel() }
    val homeViewModel = remember { HomeViewModel() }
    val accountViewModel = remember { AccountViewModel() }
    val inscripcionesViewModel = remember { InscripcionesViewModel() }
    val aluFinanzasViewModel = remember { AluFinanzasViewModel() }
    val aluCronogramaViewModel = remember { AluCronogramaViewModel() }
    val aluMallaViewModel = remember { AluMallaViewModel() }
    val aluHorarioViewModel = remember { AluHorarioViewModel() }
    val pagoOnlineViewModel = remember { PagoOnlineViewModel() }
    val aluMateriasViewModel = remember { AluMateriasViewModel() }
    val aluFacturacionViewModel = remember { AluFacturacionViewModel() }

    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MyNavigation(
                mainViewModel = mainViewModel,
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
                aluFacturacionViewModel = aluFacturacionViewModel
            )
        }
    }
}