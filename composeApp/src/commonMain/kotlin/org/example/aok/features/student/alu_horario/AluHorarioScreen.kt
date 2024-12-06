package org.example.aok.features.student.alu_horario

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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TurnedIn
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
import org.example.aok.data.network.AluHorario
import org.example.aok.data.network.Clase
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyErrorAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluHorarioScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
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
        mainViewModel = mainViewModel,
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
    val error by aluHorarioViewModel.error.collectAsState(null)

    LaunchedEffect(Unit) {
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
                    aluHorarioViewModel.clearError()
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
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
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
                        Divider()
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
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatoText("Docente: ", clase.profesor),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatoText("Aula: ", "${clase.aula} ${clase.sede}"),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatoText("Sesión: ", "${clase.sesion}"),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
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
