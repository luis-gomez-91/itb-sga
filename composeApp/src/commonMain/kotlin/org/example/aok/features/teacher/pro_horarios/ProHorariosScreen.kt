package org.example.aok.features.teacher.pro_horarios

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.aok.core.formatoText
import org.example.aok.core.parseTime
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorarioClase
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.alerts.MyErrorAlert
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun ProHorariosScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proHorariosViewModel: ProHorariosViewModel
) {
    DashBoardScreen(
        title = "Mis Horarios",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                proHorariosViewModel,
                navController
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    proHorariosViewModel: ProHorariosViewModel,
    navController: NavHostController
) {

    val data by proHorariosViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val pagerState = rememberPagerState { data.size }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val periodoSelect by homeViewModel.periodoSelect.collectAsState()
    val error by homeViewModel.error.collectAsState(null)

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
                    Clases(data[selectedTabIndex])
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
fun emptyHorarios(error: Error?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
//        verticalArrangement = Arrangement.Center
    ) {
        MyCard (
            modifier = Modifier.padding(16.dp),
            onClick = { }
        ) {
            Column(
                Modifier.fillMaxWidth().padding(32.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${error!!.title}",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${error.error}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
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
            TabRowDefaults.Indicator(
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
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
    proHorario: ProHorario
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(proHorario.clases) { clase ->
            ClaseItem(clase)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ClaseItem(
    clase: ProHorarioClase
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
//                        modifier = Modifier.weight(1f)
                    ) {
                        MyAssistChip(
                            label = "${clase.turnoComienza} - ${clase.turnoTermina}",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Filled.Timer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        MyAssistChip(
                            label = "${clase.grupo} | ${clase.nivel}",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Filled.GroupWork
                        )
                    }
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    ClaseMoreInfo(clase)
                }
            }

            if (isShowButton(clase.turnoComienza)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MyFilledTonalButton(
                        text = "Comenzar clase",
                        enabled = true,
                        icon = Icons.Filled.Start,
                        onClickAction = {

                        },
                        buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                        textColor = MaterialTheme.colorScheme.tertiary
                    )
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
        text = formatoText("Fecha inicio: ", "${clase.materiaDesde}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = formatoText("Fecha fin: ", "${clase.materiaHasta}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = formatoText("Aula: ", "${clase.aula}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )

    Text(
        text = formatoText("Carrera: ", "${clase.carrera}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

fun isShowButton(turnoComienza: String): Boolean {
    val classTime = parseTime(turnoComienza)
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    val classTimeInMinutes = classTime.hour * 60 + classTime.minute
    val currentTimeInMinutes = currentTime.hour * 60 + currentTime.minute

    val startRangeInMinutes = classTimeInMinutes - 15
    val endRangeInMinutes = classTimeInMinutes + 15

    return currentTimeInMinutes in startRangeInMinutes..endRangeInMinutes
}
