package org.itb.sga.features.teacher.pro_clases

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import org.itb.sga.data.network.ClaseX
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.Paginado
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun ProClasesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    proClasesViewModel: ProClasesViewModel
) {
    DashBoardScreen(
        title = "Mis Clases",
        navController = navController,
        content = {
            Screen(
                navController,
                homeViewModel,
                proClasesViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    proClasesViewModel: ProClasesViewModel
) {
    val data by proClasesViewModel.data.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val actualPage by homeViewModel.actualPage.collectAsState(1)
    val error by homeViewModel.error.collectAsState(null)

    LaunchedEffect(query) {
        proClasesViewModel.onloadProClases(query, actualPage, homeViewModel)
    }

    val clasesFintradas = if (query.isNotEmpty()) {
        data
//        data?.clases?.filter { it.asignatura.contains(query, ignoreCase = true) }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                clasesFintradas?.let {
                    items(it.clases) { clase ->
                        homeViewModel.homeData.value?.persona?.idDocente?.let { it1 ->
                            ClaseItem(
                                clase = clase,
                                proClasesViewModel = proClasesViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            data?.paging?.let {
                Paginado(
                    isLoading = isLoading,
                    paging = it,
                    homeViewModel = homeViewModel,
                    onBack = {
                        homeViewModel.pageLess()
                        proClasesViewModel.onloadProClases(
                            query,
                            actualPage - 1,
                            homeViewModel
                        )
                    },
                    onNext = {
                        homeViewModel.pageMore()
                        proClasesViewModel.onloadProClases(
                            query,
                            actualPage + 1,
                            homeViewModel
                        )
                    }
                )
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
fun ClaseItem(
    clase: ClaseX,
    proClasesViewModel: ProClasesViewModel,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard (
        onClick = {
            proClasesViewModel.updateClaseSelect(clase)
            proClasesViewModel.verLeccion(
                idLeccionGrupo = clase.idLeccionGrupo,
                navHostController = navController
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = clase.asignatura,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = formatoText("Grupo:", clase.grupo),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = formatoText("Fecha y apertura:", "${clase.fecha} (${clase.horaEntrada} a ${clase.horaSalida})"),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            MoreInfo(clase)
                        }
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
            Spacer(Modifier.height(4.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyAssistChip(
                    label = if (clase.abierta) "Abierta" else "Cerrada",
                    containerColor = if (clase.abierta) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.errorContainer,
                    labelColor = if (clase.abierta) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
                    icon = if (clase.abierta) Icons.Filled.Check else Icons.Filled.Cancel
                )
            }

        }

    }
}

@Composable
fun MoreInfo(
    clase: ClaseX
) {
    Text(
        text = formatoText("Turno:", clase.turno),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary
    )
    Text(
        text = formatoText("Aula:", clase.aula),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary
    )
    Text(
        text = formatoText("Asistencias:", "${clase.asistenciaCantidad} (${clase.asistenciaProciento}%)"),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}