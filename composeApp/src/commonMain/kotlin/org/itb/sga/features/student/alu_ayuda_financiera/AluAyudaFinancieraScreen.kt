package org.itb.sga.features.student.alu_ayuda_financiera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluAyudaFinancieraScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel
) {
    DashBoardScreen(
        title = "Ayuda Financiera",
        navController = navController,
        content = {
            Screen()
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Requisitos importantes para solicitar una Ayuda Financiera",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(
                text = "1. Estar matriculado al nuevo nivel de estudio y no tener valores pendientes de pago. Si necesita matriculación, puede acceder en el Sistema de Gestión Académica (SGA) módulo matrícula en línea.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "2. Revisar y actualizar todos sus datos personales en el SGA.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "3. Acceder al SGA, módulo SOLICITUD AYUDA FINANCIERA y seguir el proceso indicado con información estrictamente apegada a la verdad. Si no puede ingresar, debe enviar una solicitud (tipo: REQUERIMIENTO A TICS, descripción: NO PUEDO INGRESAR A SGA O A SOLICITUD DE AYUDA FINANCIERA).",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "4. Si tiene dudas al rellenar la solicitud, puede auxiliarse del MANUAL DE SOLICITUD DE BECA ONLINE.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Documentos que debe adjuntar a la solicitud:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        val documentos = listOf(
            "Subir cédula (ambos lados) en formato PDF (que sea lo más visible).",
            "Subir carnet de discapacidad en formato PDF (del estudiante o familiar si aplica).",
            "Subir cédula o partida de nacimiento en formato PDF (si tiene hijos).",
            "Subir rol de pago o certificado laboral donde indique el sueldo de quien cubre sus estudios en formato PDF."
        )

        documentos.forEach { documento ->
            item {
                Text(
                    text = "- $documento",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Importante:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        item {
            Text(
                text = "Debe llenar la Ficha Socio-Económica en el SGA con información estrictamente apegada a la verdad.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "Las becas se otorgan considerando el promedio y el estudio socioeconómico del estudiante.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "El estudiante es responsable de subir los requisitos al sistema antes de la fecha de vencimiento de la cuota #1 de su nivel actual.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nota:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        item {
            Text(
                text = "Debe comprar una especie tipo ayuda financiera desde la bandeja de atención. Una vez cancelada, se activará el botón del módulo de ayuda financiera para continuar con el proceso.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text(
                text = "Para cualquier aclaración adicional, acceda al módulo BANDEJA DE ATENCIÓN.",
                style = MaterialTheme.typography.bodyMedium,
//                    fontStyle = FontStyle.Italic
            )
        }
    }
}
