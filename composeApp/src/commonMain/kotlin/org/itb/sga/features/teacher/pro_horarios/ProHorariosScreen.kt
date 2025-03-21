package org.itb.sga.features.teacher.pro_horarios

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.ProHorario
import org.itb.sga.data.network.ProHorarioClase
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.features.teacher.pro_clases.ProClasesViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun ProHorariosScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proHorariosViewModel: ProHorariosViewModel,
    proClasesViewModel: ProClasesViewModel
) {
    DashBoardScreen(
        title = "Mis Horarios",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                proHorariosViewModel,
                navController,
                proClasesViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    proHorariosViewModel: ProHorariosViewModel,
    navController: NavHostController,
    proClasesViewModel: ProClasesViewModel
) {

    val data by proHorariosViewModel.data.collectAsState(emptyList())
    val error by proHorariosViewModel.error.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val periodoSelect by homeViewModel.periodoSelect.collectAsState()
    val pagerState = rememberPagerState { data.size }
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(periodoSelect) {
        selectedTabIndex = 0
        homeViewModel.homeData.value!!.persona.idDocente?.let {
            periodoSelect?.let { it1 ->
                proHorariosViewModel.onloadProHorarios(
                    id = it,
                    homeViewModel = homeViewModel,
                    periodo = it1
                )
            }
        }
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if (data.isNotEmpty()) {
                homeViewModel.clearError()
                LaunchedEffect(selectedTabIndex) {
                    pagerState.animateScrollToPage(selectedTabIndex)
                }
                LaunchedEffect(pagerState.currentPage) {
                    selectedTabIndex = pagerState.currentPage
                }
                ScrollableTabRowProHorarios(
                    proHorarios = data,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        selectedTabIndex = index
                    }
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                ) { index ->
                    Clases(data[selectedTabIndex], proHorariosViewModel, homeViewModel, navController, proClasesViewModel)
                }
            }
        }

        error?.let {
            MyErrorAlert(
                titulo = it.title,
                mensaje = it.error,
                onDismiss = {
                    proHorariosViewModel.clearError()
                },
                showAlert = true
            )
        }
    }
}

@Composable
fun ScrollableTabRowProHorarios(
    proHorarios: List<ProHorario>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        proHorarios.forEachIndexed { index, proHorario ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = proHorario.diaNombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun Clases(
    proHorario: ProHorario,
    proHorariosViewModel: ProHorariosViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    proClasesViewModel: ProClasesViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(proHorario.clases) { clase ->
            ClaseItem(clase, proHorariosViewModel, homeViewModel, navController, proClasesViewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ClaseItem(
    clase: ProHorarioClase,
    proHorariosViewModel: ProHorariosViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    proClasesViewModel: ProClasesViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard(
        Modifier,
        onClick = { }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = clase.materia,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = formatoText("Horario:", "${clase.turnoComienza} - ${clase.turnoTermina}"),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatoText("Grupo:", "${clase.grupo} (${clase.nivel})"),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    ClaseMoreInfo(clase)
                }
            }

            if (clase.claseAbierta) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MyFilledTonalButton(
                        text = "Ver clase abierta",
                        enabled = true,
                        icon = Icons.Filled.Visibility,
                        onClickAction = {
                            navController.navigate("pro_clases")
                        },
                        buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                        textColor = MaterialTheme.colorScheme.tertiary
                    )
                }
            } else {
                if (isShowButton(clase) && clase.habilitaAbrirClase) {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        MyFilledTonalButton(
                            text = "Comenzar clase",
                            enabled = true,
                            icon = Icons.Filled.Start,
                            onClickAction = {
                                homeViewModel.homeData.value?.persona?.idDocente?.let {
                                    proHorariosViewModel.comenzarClase(clase, it, navController, proClasesViewModel)
                                }
                            },
                            buttonColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            textColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClaseMoreInfo(
    clase: ProHorarioClase
) {
    Text(
        text = formatoText("Fecha inicio:", clase.materiaDesde),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = formatoText("Fecha fin:", clase.materiaHasta),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = formatoText("Aula:", clase.aula),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = formatoText("Carrera:", clase.carrera),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface
    )
}

fun isShowButton(clase: ProHorarioClase): Boolean {
    val timeZone = TimeZone.currentSystemDefault()
    val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)

    val classDateTime = parseDateTime(clase.turnoComienza, currentDateTime.date)

    if (classDateTime.date != currentDateTime.date) return false

    val classTimeInMinutes = classDateTime.time.hour * 60 + classDateTime.time.minute
    val currentTimeInMinutes = currentDateTime.time.hour * 60 + currentDateTime.time.minute

    return currentTimeInMinutes in (classTimeInMinutes - clase.tiempoAperturaAntes)..(classTimeInMinutes + clase.tiempoAperturaDespues)
}

fun parseDateTime(timeString: String, currentDate: LocalDate): LocalDateTime {
    val parts = timeString.split(":")
    if (parts.size != 2) throw IllegalArgumentException("Formato inválido, se espera HH:mm")

    val hour = parts[0].toInt()
    val minute = parts[1].toInt()

    return LocalDateTime(currentDate, LocalTime(hour, minute))
}
