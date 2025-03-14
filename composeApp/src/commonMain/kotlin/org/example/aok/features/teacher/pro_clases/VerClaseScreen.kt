package org.example.aok.features.teacher.pro_clases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.formatoText
import org.example.aok.core.logInfo
import org.example.aok.data.network.pro_clases.Asistencia
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.dashboard.DashboardScreen2

@Composable
fun VerClaseScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    proClasesViewModel: ProClasesViewModel
) {
    DashboardScreen2(
        content = { Screen(proClasesViewModel) },
        backScreen = "pro_clases",
        title = "Clase",
        navHostController = navController

    )
}

@Composable
fun Screen(
    proClasesViewModel: ProClasesViewModel
) {
    val leccionGrupo by proClasesViewModel.leccionGrupoData.collectAsState(null)
    val claseSelect by proClasesViewModel.claseSelect.collectAsState(null)


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        claseSelect?.let {
            Text(
                text = it.asignatura,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatoText("Grupo: ","${it.grupo}"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Fecha: ","${it.fecha} (${it.horaEntrada} - ${it.horaSalida}"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Turno: ","${it.turno}"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Aula: ","${it.aula}"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Listado de alumnos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            leccionGrupo?.let {
                items(it.asistencias) { asistencia ->
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    AsistenciaItem(asistencia, proClasesViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }


}

@Composable
fun AsistenciaItem(
    asistencia: Asistencia,
    proClasesViewModel: ProClasesViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row (
            modifier =  Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = asistencia.alumno,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row {
                MyAssistChip(
                    label = "12/15",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Asistencia",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
