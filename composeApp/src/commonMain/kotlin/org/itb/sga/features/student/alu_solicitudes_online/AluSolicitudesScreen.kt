package org.itb.sga.features.student.alu_solicitudes_online

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.CircularProgressIndicator
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
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.AluSolicitud
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAddButton
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun AluSolicitudesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    aluSolicitudesViewModel: AluSolicitudesViewModel
) {
    DashBoardScreen(
        title = "Solicitudes en línea",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                aluSolicitudesViewModel,
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
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    navController: NavHostController
) {
    val data by aluSolicitudesViewModel.data.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val uploadFormLoading by aluSolicitudesViewModel.uploadFormLoading.collectAsState(false)
    val error by homeViewModel.error.collectAsState(null)

    LaunchedEffect(query) {
        homeViewModel.homeData.value?.persona?.idInscripcion?.let {
            aluSolicitudesViewModel.onloadAluSolicitudes(
                it,
                homeViewModel
            )
        }
    }

    val filterData = if (query.isNotEmpty()) {
        data
//        data?.clases?.filter { it.asignatura.contains(query, ignoreCase = true) }
    } else {
        data
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (filterData.isEmpty()) {
                    Text(
                        text = "No existen solicitudes generadas",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(data) { solicitud ->
                            CardAluFinanza(
                                solicitud = solicitud
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .align(Alignment.BottomEnd)
            ) {
                if (uploadFormLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    MyAddButton(
                        modifier = Modifier.align(Alignment.Center),
                        onclick = {
                            navController.navigate("addSolicitud")
                        }
                    )
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
fun CardAluFinanza(
    solicitud: AluSolicitud
) {
    var expanded by remember { mutableStateOf(false) }

    MyCard (
        modifier = Modifier.padding(bottom = 4.dp),
        onClick = { }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    solicitud.tipoEspecie?.let {
                        Text(
                            text = it,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row() {
                        MyAssistChip(
                            label = solicitud.fecha,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Filled.DateRange
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        MyAssistChip(
                            label = "${solicitud.numeroSerie}",
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Filled.Numbers
                        )
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

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    MoreInfo(solicitud)
                }
            }

        }

    }
}

@Composable
fun MoreInfo(
    solicitud: AluSolicitud
) {
    Text(
        text = formatoText("Observación: ", "${solicitud.observacion}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Departamento: ", "${solicitud.departamento}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Asignado a: ", "${solicitud.usuarioAsignado}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
//    Text(
//        text = formatoText("Valor: ", "${solicitud.valor}"),
//        fontSize = 10.sp,
//        color = MaterialTheme.colorScheme.onSurface
//    )
//    Text(
//        text = formatoText("Pagado: ", "${solicitud.pagado}"),
//        fontSize = 10.sp,
//        color = MaterialTheme.colorScheme.onSurface
//    )
    Text(
        text = formatoText("Autorizado: ", "${solicitud.autorizado}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Resolución: ", "${solicitud.resolucion}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Respuesta: ", "${solicitud.respuesta}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = formatoText("Respuesta docente: ", "${solicitud.respuestaDocente}"),
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

