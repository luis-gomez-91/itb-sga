package org.example.aok.features.student.alu_cronograma

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.core.capitalizeWords
import org.example.aok.core.formatoText
import org.example.aok.data.network.AluCronograma
import org.example.aok.data.network.Horario
import org.example.aok.data.network.Profesor
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyErrorAlert
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluCronogramaScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluCronogramaViewModel: AluCronogramaViewModel
) {
    DashBoardScreen(
        title = "Mi Cronograma",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluCronogramaViewModel,
                navController
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluCronogramaViewModel: AluCronogramaViewModel,
    navController: NavHostController
) {
    val data: List<AluCronograma> by aluCronogramaViewModel.data.collectAsState(emptyList())
    val error by homeViewModel.error.collectAsState(null)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val isLoading by homeViewModel.isLoading.collectAsState(false)

    val dataFiltada = if (searchQuery.isNotEmpty()) {
        data.filter { it.asignatura.contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        aluCronogramaViewModel.onloadAluCronograma(homeViewModel.homeData.value!!.persona.idInscripcion!!, homeViewModel)
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            items(dataFiltada) { data ->
                AluCronogramaItem(data)
                Spacer(modifier = Modifier.height(8.dp))
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
fun AluCronogramaItem(
    data: AluCronograma
) {
    var expanded by remember { mutableStateOf(false) }
    MyCard(
        modifier = Modifier.padding(bottom = 4.dp),
        onClick = { },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        borderColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.asignatura,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand"
                        )
                    }
                }
            }

            MyAssistChip(
                label = formatoText("", "${data.inicio} - ${data.fin}").toString(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                labelColor = MaterialTheme.colorScheme.primary,
                icon = Icons.Filled.CalendarMonth
            )

            Row() {
                MyAssistChip(
                    label = "${data.horas} horas",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = "${data.creditos} créditos",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary
                )
            }


            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Profesores(data.profesores)

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Horarios(data.horarios)
                }
            }

        }
    }
}

@Composable
fun Profesores(profesores: List<Profesor>) {
    Column() {
        Text(text = "Profesores", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            items(profesores) { profesor ->
                ProfesorItem(profesor)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun ProfesorItem(profesor: Profesor) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column (
            modifier = Modifier
                .padding(8.dp)
        )
        {
            Text(
                text = profesor.profesor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            MyAssistChip(
                label = formatoText("", "${profesor.desde} - ${profesor.hasta}").toString(),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.secondary,
                icon = Icons.Filled.Person2
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                MyAssistChip(
                    label = if (profesor.auxiliar) "Auxiliar" else "Titular",
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Filled.Person2
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = profesor.segmento.capitalizeWords(),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    labelColor = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Default.Book
                )
            }
        }
    }
}

@Composable
fun Horarios(horarios: List<Horario>) {
    Column() {
        Text(text = "Horarios", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            items(horarios) { horario ->
                HorarioItem(horario)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun HorarioItem(horario: Horario) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        )
        {
            Text(
                text = horario.dia,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            MyAssistChip(
                label = "${horario.turnoComienza} - ${horario.turnoTermina}",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.secondary
            )
            MyAssistChip(
                label = "Aula ${horario.aula}",
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                labelColor = MaterialTheme.colorScheme.tertiary
            )
            MyAssistChip(
                label = if (horario.claseVirtual) "Clase virtual" else "Clase presencial",
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                labelColor = MaterialTheme.colorScheme.tertiary
            )

        }
    }
}