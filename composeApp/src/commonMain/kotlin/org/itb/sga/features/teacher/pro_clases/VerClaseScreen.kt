package org.itb.sga.features.teacher.pro_clases

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
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
import org.itb.sga.data.network.pro_clases.Asistencia
import org.itb.sga.data.network.pro_clases.LeccionGrupo
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextFieldArea
import org.itb.sga.ui.components.MySwitch
import org.itb.sga.ui.components.dashboard.DashboardScreen2

@Composable
fun VerClaseScreen(
    navController: NavHostController,
    proClasesViewModel: ProClasesViewModel
) {
    DashboardScreen2(
        content = { Screen(proClasesViewModel, navController) },
        title = "Clase",
        onBack = {
            navController.navigate("pro_clases")
//            proClasesViewModel.updateClaseSelect(null)
        }
    )
}

@Composable
fun Screen(
    proClasesViewModel: ProClasesViewModel,
    navController: NavHostController
) {
    val leccionGrupo by proClasesViewModel.leccionGrupoData.collectAsState(null)
    val claseSelect by proClasesViewModel.claseSelect.collectAsState(null)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        claseSelect?.let {
            Text(
                text = it.asignatura,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatoText("Grupo:", it.grupo),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Fecha:","${it.fecha} (${it.horaEntrada} - ${it.horaSalida}"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Turno:", it.turno),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Aula:", it.aula),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(Modifier.height(8.dp))

            leccionGrupo?.let { itLeccionGruopo ->
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ListadoAlumnos(itLeccionGruopo, it, proClasesViewModel)
                    ContenidoItem(itLeccionGruopo, it, proClasesViewModel)
                    ObservacionesItem(itLeccionGruopo, it, proClasesViewModel)
                    if (it.abierta) {
                        Spacer(Modifier.height(16.dp))
                        MyFilledTonalButton(
                            text = "Cerrar clase",
                            enabled = true,
                            onClickAction = {
                                proClasesViewModel.updateValueAction("", "cerrarClase")
                                navController.popBackStack()
                            },
                            buttonColor = MaterialTheme.colorScheme.errorContainer,
                            textColor = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }


        }


    }
}

@Composable
fun ListadoAlumnos(
    leccionGrupo: LeccionGrupo,
    claseSelect: ClaseX,
    proClasesViewModel: ProClasesViewModel
) {
    var expandedAsistencias by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Listado de alumnos",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        AnimatedContent(targetState = expandedAsistencias) { isExpanded ->
            IconButton(onClick = { expandedAsistencias = !expandedAsistencias }) {
                Icon(
                    imageVector = if (expandedAsistencias) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expandedAsistencias) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    AnimatedVisibility(
        visible = expandedAsistencias,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Spacer(Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            leccionGrupo.let { leccion ->
                itemsIndexed(leccion.asistencias) { index, asistencia ->
                    Spacer(modifier = Modifier.height(8.dp))
                    AsistenciaItem(
                        index = index,
                        asistencia = asistencia,
                        proClasesViewModel = proClasesViewModel,
                        leccionGrupo = leccion,
                        claseSelect = claseSelect
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ContenidoItem(
    leccionGrupo: LeccionGrupo,
    claseSelect: ClaseX,
    proClasesViewModel: ProClasesViewModel
) {
    var expandedContenido by remember { mutableStateOf(false) }
    var contenido by remember { mutableStateOf(leccionGrupo.leccionGrupoContenido) }

    LaunchedEffect(leccionGrupo) {
        contenido = leccionGrupo.leccionGrupoContenido
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Contenido",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        AnimatedContent(targetState = expandedContenido) { isExpanded ->
            IconButton(onClick = { expandedContenido = !expandedContenido }) {
                Icon(
                    imageVector = if (expandedContenido) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expandedContenido) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    AnimatedVisibility(
        visible = expandedContenido,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        MyOutlinedTextFieldArea(
            value = contenido,
            onValueChange = { contenido = it },
            label = "Contenido",
            modifier = Modifier.fillMaxWidth(),
            enabled = claseSelect.abierta,
            onFocusLost = {
                if (contenido != leccionGrupo.leccionGrupoContenido) {
                    proClasesViewModel.updateValueAction(contenido, "updateContenido")
                }
            }
        )
    }
}

@Composable
fun ObservacionesItem(
    leccionGrupo: LeccionGrupo,
    claseSelect: ClaseX,
    proClasesViewModel: ProClasesViewModel
) {
    var expandedObservaciones by remember { mutableStateOf(false) }
    var observaciones by remember { mutableStateOf(leccionGrupo.leccionGrupoObservaciones) }

    LaunchedEffect(leccionGrupo) {
        observaciones = leccionGrupo.leccionGrupoObservaciones
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Observaciones",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        AnimatedContent(targetState = expandedObservaciones) { isExpanded ->
            IconButton(onClick = { expandedObservaciones = !expandedObservaciones }) {
                Icon(
                    imageVector = if (expandedObservaciones) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expandedObservaciones) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
    AnimatedVisibility(
        visible = expandedObservaciones,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        MyOutlinedTextFieldArea(
            value = observaciones,
            onValueChange = { observaciones = it },
            label = "Observaciones",
            modifier = Modifier.fillMaxWidth(),
            enabled = claseSelect.abierta,
            onFocusLost = {
                if (observaciones != leccionGrupo.leccionGrupoObservaciones) {
                    proClasesViewModel.updateValueAction(observaciones, "updateObservaciones")
                }
            }
        )
    }
}

@Composable
fun AsistenciaItem(
    index: Int,
    asistencia: Asistencia,
    proClasesViewModel: ProClasesViewModel,
    leccionGrupo: LeccionGrupo,
    claseSelect: ClaseX,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row (
            modifier =  Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = asistencia.alumno,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.width(8.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyAssistChip(
                    label = "${asistencia.cantidad}/${leccionGrupo.totalLecciones} (${asistencia.porcentaje}%)",
                    containerColor = if (asistencia.porcentaje >= leccionGrupo.minimoAsistencia) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.errorContainer,
                    labelColor = if (asistencia.porcentaje >= leccionGrupo.minimoAsistencia) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(8.dp))
                if (!claseSelect.abierta) {
                    Icon(
                        imageVector = if (asistencia.asistio) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                        contentDescription = "Asistencia",
                        tint = if (asistencia.asistio) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.error
                    )
                } else {
                    MySwitch(
                        checked = asistencia.asistio,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        onCheckedChange = { checked ->
                            proClasesViewModel.updateAsistencia(asistencia, checked, index)
                        },
                        enabled = claseSelect.abierta
                    )
                }
            }
        }
    }
}
