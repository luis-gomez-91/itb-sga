package org.itb.sga.features.student.alu_materias

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.data.network.AluMateria
import org.itb.sga.data.network.AluMateriaCalificacion
import org.itb.sga.data.network.AluMateriaLeccion
import org.itb.sga.data.network.AluMateriaProfesor
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.features.student.alu_malla.CardStyle
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyOutlinedTextField
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
        homeViewModel.homeData.value?.persona?.idInscripcion?.let {
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

        error?.let {
            MyErrorAlert(
                titulo = it.title,
                mensaje = it.error,
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
            SecondaryIndicator(
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
                        text = aluMateria.materiaNombre,
                        style = MaterialTheme.typography.titleSmall,
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
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Del ${aluMateria.materiaInicio} al ${aluMateria.materiaFin}",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                MyAssistChip(
                    label = aluMateria.estado,
                    containerColor = estadoStyle.containerColor,
                    labelColor = estadoStyle.color,
                    icon = estadoStyle.icono
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = "${aluMateria.notaFinal} puntos",
                    containerColor = estadoStyle.containerColor,
                    labelColor = estadoStyle.color
                )
                Spacer(modifier = Modifier.width(4.dp))
                MyAssistChip(
                    label = "${aluMateria.asistencias}% de asistencia",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            DocentesItem(materia.profesores)

            materia.calificaciones?.let { calificaciones ->
                Spacer(Modifier.height(8.dp))
                CalificacionesItem(calificaciones)
            }
            materia.lecciones?.let { lecciones ->
                Spacer(Modifier.height(8.dp))
                LeccionesItem(lecciones)
            }
        }

    } ?: run {
        Text(text = "No hay asignaturas disponibles", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DocentesItem(
    docentes: List<AluMateriaProfesor>?
) {
    var expanded by remember { mutableStateOf(true) }

    MyCard (
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Docentes asignados",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            modifier = Modifier.rotate(if (expanded) 180f else 0f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Spacer(Modifier.height(8.dp))
                if (docentes != null) {
                    LazyColumn (
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                    ) {
                        items(docentes) { docente ->
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = docente.profesorNombre,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(Modifier.width(8.dp))

                                MyAssistChip(
                                    label = docente.rol,
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    labelColor = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                } else {
                    Text(
                        text = "Por definir",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CalificacionesItem(
    calificacion: AluMateriaCalificacion
) {
    var expanded by remember { mutableStateOf(false) }

    val calificacionesMap = mapOf(
        "n1" to ("N1" to (calificacion.n1.toString())),
        "n2" to ("N2" to (calificacion.n2.toString())),
        "n3" to ("N3" to (calificacion.n3.toString())),
        "n4" to ("N4" to (calificacion.n4.toString())),
//        "parcial" to ("Parc." to (calificacion.parcial?.toString() ?: "")),
        "examen" to ("Ex." to (calificacion.examen.toString())),
        "recuperacion" to ("Recup." to (calificacion.recuperacion.toString())),
        "total" to ("Tot." to (calificacion.total?.toString() ?: ""))
    )

    val textStyle = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        textAlign = TextAlign.Center
    )

    MyCard (
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Calificaciones",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(8.dp))

                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            modifier = Modifier.rotate(if (expanded) 180f else 0f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Spacer(Modifier.height(16.dp))

                LazyRow (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(calificacionesMap.toList()) { (_, calificacion) ->
                        val (label, nota) = calificacion

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                            MyOutlinedTextField(
                                value = nota,
                                onValueChange = {},
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(70.dp),
                                enabled = false,
                                textStyle = textStyle
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LeccionesItem(
    lecciones: List<AluMateriaLeccion>
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard (
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Listado de asistencias",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(8.dp))

                AnimatedContent(targetState = expanded) { isExpanded ->
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = if (expanded) "Collapse" else "Expand",
                            modifier = Modifier.rotate(if (expanded) 180f else 0f),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(lecciones) { leccion ->
                        LeccionItem(leccion)
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun LeccionItem(
    leccion: AluMateriaLeccion
) {
    Text(
        text = leccion.fecha,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.height(4.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        val horaDisplay = if (leccion.horaSalida != null) {
            "${leccion.horaEntrada} - ${leccion.horaSalida}"
        } else {
            "${leccion.horaEntrada} - Cursando"
        }
        leccion.horaSalida.let {
            MyAssistChip(
                label = horaDisplay,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.secondary,
                icon = Icons.Filled.Timer
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        MyAssistChip(
            label = "Asistencia",
            containerColor = if (leccion.asistio) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.errorContainer,
            labelColor = if (leccion.asistio) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
            icon = if (leccion.asistio) Icons.Filled.Check else Icons.Filled.Close
        )
    }
}
