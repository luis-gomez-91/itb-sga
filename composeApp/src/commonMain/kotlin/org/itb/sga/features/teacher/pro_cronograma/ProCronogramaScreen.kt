package org.itb.sga.features.teacher.pro_cronograma

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.pro_cronograma.ProCronograma
import org.itb.sga.data.network.pro_cronograma.ProCronogramaHorarios
import org.itb.sga.data.network.pro_cronograma.ProCronogramaProfesores
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun ProCronogramaScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proCronogramaViewModel: ProCronogramaViewModel
) {
    DashBoardScreen(
        title = "Cronograma",
        navController = navController,
        content = {
            Screen(
                proCronogramaViewModel,
                homeViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    proCronogramaViewModel: ProCronogramaViewModel,
    homeViewModel: HomeViewModel
) {
    val data by proCronogramaViewModel.data.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        homeViewModel.homeData.value?.persona?.idDocente?.let {
            proCronogramaViewModel.onloadProHorarios(
                it, homeViewModel
            )
        }
    }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(data) { index, cronograma ->
            CronogramaItem(index, proCronogramaViewModel, cronograma)
            HorizontalDivider()
        }
    }
}

@Composable
fun CronogramaItem (
    index: Int,
    proCronogramaViewModel: ProCronogramaViewModel,
    cronograma: ProCronograma
) {
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = cronograma.asignatura,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(8.dp))
            AnimatedContent(targetState = expanded) { isExpanded ->
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
        }

        Text(
            text = formatoText("Grupo: ", "${cronograma.grupo} (${cronograma.nivelMalla})"),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = formatoText("Fecha: ", "${cronograma.fechaDesde} a ${cronograma.fechaHasta}"),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = formatoText("Carrera: ", cronograma.carrera),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(Modifier.height(8.dp))
                Profesores(cronograma.profesores)
                Spacer(Modifier.height(8.dp))
                Horarios(cronograma.horarios)
            }
        }
        Spacer(Modifier.height(4.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MyFilledTonalButton(
                text = if (true) "Aceptar materia" else "Rechazar materia",
                enabled = true,
                onClickAction = {

                },
                icon = if (true) Icons.Filled.Add else Icons.Filled.Cancel,
                buttonColor = if (true) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.errorContainer,
                textColor = if (true) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
            )
        }
    }
}
@Composable
fun Horarios (
    horarios: List<ProCronogramaHorarios>
) {
    Text(
        text = "Horarios de clase",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
    )
    Spacer(Modifier.height(4.dp))
    LazyRow (
        modifier = Modifier.fillMaxWidth()
    ) {
        items(horarios) { horario ->
            HorarioItem(horario)
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
fun HorarioItem(horario: ProCronogramaHorarios) {
    MyCard {
        Column {
            Text(
                text = horario.dia,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = formatoText("Horario: ", horario.horario),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatoText("Aula: ", horario.aula),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}


@Composable
fun Profesores (
    profesores: List<ProCronogramaProfesores>
) {
    Text(
        text = "Docentes Asignados",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
    )
    Spacer(Modifier.height(4.dp))
    LazyRow (
        modifier = Modifier.fillMaxWidth()
    ) {
        items(profesores) { profesor ->
            ProfesorItem(profesor)
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
fun ProfesorItem(
    profesor: ProCronogramaProfesores
) {
    Spacer(Modifier.height(8.dp))
    MyCard {
        Column {
            Text(
                text = profesor.profesor,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = formatoText("Fecha: ", "${profesor.desde} a ${profesor.hasta}"),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatoText("Segmento: ", profesor.segmento),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            MyAssistChip(
                label = profesor.log,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                icon = Icons.Filled.DateRange
            )
        }
    }

}
