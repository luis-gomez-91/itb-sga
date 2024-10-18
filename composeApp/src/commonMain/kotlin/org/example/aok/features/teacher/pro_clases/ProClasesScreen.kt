package org.example.aok.features.teacher.pro_clases

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.MoreVert
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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.aok.core.MainViewModel
import org.example.aok.core.formatoText
import org.example.aok.core.parseTime
import org.example.aok.data.network.ClaseX
import org.example.aok.data.network.Paging
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun ProClasesScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
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
                proClasesViewModel,
                loginViewModel
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    proClasesViewModel: ProClasesViewModel,
    loginViewModel: LoginViewModel
) {
    val data by proClasesViewModel.data.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val actualPage by homeViewModel.actualPage.collectAsState(2)

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
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                clasesFintradas?.let {
                    items(it.clases) { clase ->
                        ClaseItem(
                            clase = clase
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            data?.let {
                Paginado(
                    homeViewModel,
                    proClasesViewModel,
                    query,
                    isLoading,
                    it.paging
                )
            }
        }
    }
}

@Composable
fun Paginado(
    homeViewModel: HomeViewModel,
    proClasesViewModel: ProClasesViewModel,
    query: String,
    isLoading: Boolean,
    paging: Paging
) {
    val actualPage by homeViewModel.actualPage.collectAsState(1)

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {
                homeViewModel.pageLess()
                proClasesViewModel.onloadProClases(
                    query,
                    actualPage,
                    homeViewModel
                )
            },
            enabled = actualPage > 1  && !isLoading
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "Back",
                tint = if (actualPage > 1  && !isLoading) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant
            )
        }

        Text(
            text = "${actualPage}/${paging.lastPage}",
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )

        IconButton(
            onClick = {
                homeViewModel.pageMore()
                proClasesViewModel.onloadProClases(
                    query,
                    actualPage,
                    homeViewModel
                )
            },
            enabled = actualPage < (paging.lastPage) && !isLoading
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "Next",
                tint = if (actualPage < (paging.lastPage)  && !isLoading) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun ClaseItem(
    clase: ClaseX
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard (
        onClick = { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = clase.asignatura,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = clase.turno,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    MyAssistChip(
                        label = clase.fecha,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        labelColor = MaterialTheme.colorScheme.primary,
                        icon = Icons.Filled.DateRange
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    MyAssistChip(
                        label = clase.grupo,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        labelColor = MaterialTheme.colorScheme.primary,
                        icon = Icons.Filled.GroupWork
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MyAssistChip(
                        label = if (clase.abierta) "Abierta" else "Cerrada",
                        containerColor = if (clase.abierta) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.errorContainer,
                        labelColor = if (clase.abierta) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error,
                        icon = if (clase.abierta) Icons.Filled.Check else Icons.Filled.Cancel
                    )
                }
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Acciones"
                    )
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
                    MoreInfo(clase)
                }
            }

        }

    }
}


@Composable
fun MoreInfo(
    clase: ClaseX
) {
    Text(
        text = formatoText("Aula: ", "${clase.aula}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Hora entrada: ", "${clase.horaEntrada}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Hora salida: ", "${clase.horaSalida}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Asistencia: ", "${clase.asistenciaCantidad} (${clase.asistenciaProciento}%)"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}