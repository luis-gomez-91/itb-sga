package org.itb.sga.features.student.alu_materias

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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.itb.sga.data.network.AluMateria
import org.itb.sga.data.network.AluMateriaLeccion
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.features.student.alu_malla.CardStyle
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluMateriasScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluMateriasViewModel: AluMateriasViewModel
) {
    DashBoardScreen(
        title = "Mis Materias",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluMateriasViewModel,
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
    aluMateriasViewModel: AluMateriasViewModel,
    navController: NavHostController
) {
    val data: List<AluMateria> by aluMateriasViewModel.data.collectAsState(emptyList())
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val pagerState = rememberPagerState { data.size }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val error by homeViewModel.error.collectAsState(null)

    val dataFiltada = if (searchQuery.isNotEmpty()) {
        data.filter { it.materiaNombre .contains(searchQuery, ignoreCase = true) }
    } else {
        data
    }

    LaunchedEffect(Unit) {
        homeViewModel.clearError()
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            aluMateriasViewModel.onloadAluMaterias(
                it, homeViewModel
            )
        }
    }

    LaunchedEffect(data) {
        selectedTabIndex = 0
        pagerState.scrollToPage(0)
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            if (dataFiltada.isNotEmpty()) {
                LaunchedEffect(selectedTabIndex) {
                    pagerState.animateScrollToPage(selectedTabIndex)
                }
                LaunchedEffect(pagerState.currentPage) {
                    selectedTabIndex = pagerState.currentPage
                }
                ScrollableTabRowAluMateria(
                    data = data,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        selectedTabIndex = index
                    }
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                ) { index ->
                    Column (
                        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surface)
                    ){
                        AluMateriaItem(data[selectedTabIndex])
                    }
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
fun ScrollableTabRowAluMateria(
    data: List<AluMateria>,
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
        data.forEachIndexed { index, aluMateria ->
            Tab(
                modifier = Modifier.fillMaxWidth(0.5f),
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                },
                text = {
                    Text(
                        text = aluMateria.materiaNombre ?: "N/A",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
fun AluMateriaItem(
    aluMateria: AluMateria?
) {
    aluMateria?.let { materia ->
        val estadoStyle = when {
            aluMateria.estado == "APROBADO" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                color = MaterialTheme.colorScheme.onPrimary,
                icono = Icons.Filled.CheckCircle,
                estado = "APROBADO"
            )
            aluMateria.estado == "REPROBADO" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                color = MaterialTheme.colorScheme.error,
                icono = Icons.Filled.Close,
                estado = "REPROBADO"
            )
            aluMateria.estado == "RECUPERACION" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                color = MaterialTheme.colorScheme.tertiary,
                icono = Icons.Filled.Error,
                estado = "RECUPERACION"
            )
            aluMateria.estado == "EXAMEN" -> CardStyle(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.primary,
                icono = Icons.Filled.Book,
                estado = "EXAMEN"
            )
            else -> CardStyle(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                color = MaterialTheme.colorScheme.onSurface,
                icono = Icons.Filled.Pending,
                estado = "EN CURSO"
            )
        }


        Column (
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Del ${aluMateria.materiaInicio} al ${aluMateria.materiaFin}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                MyAssistChip(
                    label = "Estado: ${aluMateria.estado}",
                    containerColor = estadoStyle.containerColor,
                    labelColor = estadoStyle.color,
                    icon = estadoStyle.icono
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = "${aluMateria.numAsistencias} de ${aluMateria.numLecciones} asistencias: ${aluMateria.asistencias}%",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Filled.School
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize().background(color = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            items(materia.lecciones) { leccion ->
                LeccionItem(leccion)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    } ?: run {
        Text(text = "No hay asignaturas disponibles", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun LeccionItem(
    leccion: AluMateriaLeccion
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.Top,
    ){
        Text(
            text = leccion.fecha,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            MyAssistChip(
                label = "${leccion.horaEntrada} - ${leccion.horaSalida}",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.secondary,
                icon = Icons.Filled.Timer
            )
            Spacer(modifier = Modifier.width(4.dp))
            MyAssistChip(
                label = "Asistencia",
                containerColor = if (leccion.asistio) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.errorContainer,
                labelColor = if (leccion.asistio) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
                icon = if (leccion.asistio) Icons.Filled.Check else Icons.Filled.Close
            )
        }
    }
    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)
}
