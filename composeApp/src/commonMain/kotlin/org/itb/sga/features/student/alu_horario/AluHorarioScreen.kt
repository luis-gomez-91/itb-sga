package org.itb.sga.features.student.alu_horario

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.AluHorario
import org.itb.sga.data.network.Clase
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluHorarioScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluHorarioViewModel: AluHorarioViewModel
) {
    DashBoardScreen(
        title = "Mis Horarios",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluHorarioViewModel,
                navController
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluHorarioViewModel: AluHorarioViewModel,
    navController: NavHostController
) {
    val data by aluHorarioViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val error by homeViewModel.error.collectAsState(null)

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluHorarioViewModel.onloadAluHorario(
                it, homeViewModel
            )
        }
    }

    val horariosFiltrados = if (searchQuery.isNotEmpty()) {
        data.filter { it.materiaNombre.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(horariosFiltrados) { horario ->
                    HorarioItem(
                        horario = horario
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if (error != null) {
            MyErrorAlert(
                titulo = error!!.title,
                mensaje = error!!.error,
                onDismiss = {
                    homeViewModel.clearError()
                    navController.popBackStack()
                },
                showAlert = true
            )
        }
    }
}

@Composable
fun HorarioItem(
    horario: AluHorario
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard (
        modifier = Modifier.padding(bottom = 4.dp),
        onClick = { }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = horario.materiaNombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Row() {
                    MyAssistChip(
                        label = "${horario.materiaInicio} - ${horario.materiaFin}",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary,
                        icon = Icons.Filled.DateRange
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MyAssistChip(
                        label = "${horario.paralelo} - ${horario.nivelMalla}",
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary
//                        icon = Icons.Filled.GroupWork
                    )
                }

                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        Clases(horario.clases)
                    }
                }
            }


            AnimatedContent(targetState = expanded) { isExpanded ->
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
        }


    }
}

@Composable
fun Clases(
    clases: List<Clase>
) {
    Column() {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            items(clases) { clase ->
                ClaseItem(clase)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ClaseItem(
    clase: Clase
) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column (
            modifier = Modifier.padding(8.dp)
        )
        {
            Text(
                text = clase.dia,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = formatoText("Docente:", clase.profesor),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = formatoText("Aula:", "${clase.aula} ${clase.sede}"),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = formatoText("Sesión:", clase.sesion),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                MyAssistChip(
                    label = "${clase.turnoComienza} - ${clase.turnoTermina}",
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Filled.Timelapse
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = "${clase.turnoHoras} horas",
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Filled.Timer
                )
            }
        }
    }
}
