package org.example.aok.features.student.alu_solicitudes_online

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.core.formatoText
import org.example.aok.data.network.AluSolicitud
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAddButton
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun AluSolicitudesScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
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
                aluSolicitudesViewModel
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    aluSolicitudesViewModel: AluSolicitudesViewModel
) {
    val data by aluSolicitudesViewModel.data.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val showForm by aluSolicitudesViewModel.showForm.collectAsState(false)

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
        if (false) {
            AddSolicitudScreen(
                aluSolicitudesViewModel = aluSolicitudesViewModel,
                homeViewModel = homeViewModel
            )
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
                    //            Spacer(modifier = Modifier.height(8.dp))
                }
                MyAddButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onclick = {
                        aluSolicitudesViewModel.changeShowForm()
                    }
                )
            }
            if (showForm) {
                addSolicitudForm(aluSolicitudesViewModel, homeViewModel)
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addSolicitudForm(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    homeViewModel: HomeViewModel
) {
    var expandedDepartamento by remember { mutableStateOf(false) }
    var expandedTipoSolicitud by remember { mutableStateOf(false) }
    val departamentos by aluSolicitudesViewModel.departamentos.collectAsState(emptyList())
    val selectedDepartamento by aluSolicitudesViewModel.selectedDepartamento.collectAsState(null)
    val selectedTipoSolicitud by aluSolicitudesViewModel.selectedTipoSolicitud.collectAsState(null)

    LaunchedEffect(departamentos) {
        if (departamentos.isEmpty()) {
            aluSolicitudesViewModel.onloadAddForm(homeViewModel)
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            aluSolicitudesViewModel.changeShowForm()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Nueva solicitud",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = expandedDepartamento,
                onExpandedChange = { expandedDepartamento = it },
            ) {
                TextField(
                    value = selectedDepartamento?.nombre ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(text = "Departamento") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDepartamento) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 10.sp)
                )

                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expandedDepartamento,
                    onDismissRequest = { expandedDepartamento = false }
                ) {
                    departamentos.forEach { departamento ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = {
                                Text(
                                    departamento.nombre,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                aluSolicitudesViewModel.changeSelectedDepartamento(departamento)
                                expandedDepartamento = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = expandedTipoSolicitud,
                onExpandedChange = { if (selectedDepartamento != null) expandedTipoSolicitud = it },
            ) {
                TextField(
                    value = selectedDepartamento?.especies?.find { it == selectedTipoSolicitud }?.nombre ?: "",
                    onValueChange = { },
                    readOnly = true,
                    enabled = selectedDepartamento != null,
                    label = { Text(text = "Tipo solicitud") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipoSolicitud) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 10.sp)
                )

                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expandedTipoSolicitud,
                    onDismissRequest = { expandedTipoSolicitud = false }
                ) {
                    selectedDepartamento?.especies?.forEach { tipoSolicitud ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = {
                                Text(
                                    tipoSolicitud.nombre,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                aluSolicitudesViewModel.changeSelectedTipoEspecie(tipoSolicitud)
                                expandedTipoSolicitud = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Observaciones") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                textStyle = TextStyle(fontSize = 14.sp),
                maxLines = 5,
                singleLine = false,
                shape = RoundedCornerShape(8.dp),
            )
        }
    }
}