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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.formatoText
import org.example.aok.data.network.ClaseX
import org.example.aok.data.network.pro_clases.Asistencia
import org.example.aok.data.network.pro_clases.LeccionGrupo
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyOutlinedTextFieldArea
import org.example.aok.ui.components.MySwitch
import org.example.aok.ui.components.dashboard.DashboardScreen2

@Composable
fun VerClaseScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    proClasesViewModel: ProClasesViewModel
) {
    DashboardScreen2(
        content = { Screen(proClasesViewModel) },
        backScreen = "pro_clases",
        title = "",
        navHostController = navController
    )
}

@Composable
fun Screen(
    proClasesViewModel: ProClasesViewModel
) {
    val leccionGrupo by proClasesViewModel.leccionGrupoData.collectAsState(null)
    val claseSelect by proClasesViewModel.claseSelect.collectAsState(null)
    var expandedAsistencias by remember { mutableStateOf(false) }
    var expandedContenido by remember { mutableStateOf(true) }
    var expandedObservaciones by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        claseSelect?.let {
            Text(
                text = it.asignatura,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatoText("Grupo: ", "${it.grupo}"),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText(
                    "Fecha y apertura: ",
                    "${it.fecha} (${it.horaEntrada} a ${it.horaSalida})"
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Turno: ", it.turno),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = formatoText("Aula: ", it.aula),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )


            Spacer(Modifier.height(16.dp))

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
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                ) {
                    leccionGrupo?.let { leccion ->
                        itemsIndexed(leccion.asistencias) { index, asistencia ->
                            Spacer(modifier = Modifier.height(8.dp))
                            AsistenciaItem(index, asistencia, proClasesViewModel, leccion, it)
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
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
                value = leccionGrupo?.leccionGrupoContenido ?: "",
                onValueChange = { },
                label = "Contenido",
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
        }

        Spacer(Modifier.height(8.dp))
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
                value = leccionGrupo?.leccionGrupoObservaciones ?: "",
                onValueChange = { },
                label = "Observaciones",
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
        }
    }
}

@Composable
fun AsistenciaItem(
    index: Int,
    asistencia: Asistencia,
    proClasesViewModel: ProClasesViewModel,
    leccionGrupo: LeccionGrupo,
    claseSelect: ClaseX
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
                text = asistencia.alumno,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

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
