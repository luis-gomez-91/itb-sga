package org.itb.sga.features.teacher.pro_evaluaciones

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.itb.sga.core.formatoText
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluacionesCalificacion
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluacionesMateria
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashboardScreen2

@Composable
fun ProCalificacionesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    DashboardScreen2(
        content = { Screen(proEvaluacionesViewModel, homeViewModel) },
        title = "Calificaciones",
        onBack = {
            navController.navigate("pro_evaluaciones")
        }
    )
}

@Composable
fun Screen(
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    homeViewModel: HomeViewModel
) {
    val data by proEvaluacionesViewModel.data.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val error by proEvaluacionesViewModel.error.collectAsState(null)
    val materiaSelect by proEvaluacionesViewModel.materiaSelect.collectAsState(null)
    var showAll by remember { mutableStateOf(false) }

    LaunchedEffect(materiaSelect) {
        homeViewModel.homeData.value!!.persona.idDocente?.let {
            proEvaluacionesViewModel.onloadProEvaluaciones(
                id = it,
                homeViewModel = homeViewModel,
            )
        }
    }

    val dataFilter = if (query.isNotEmpty()) {
        data?.alumnos?.filter { item ->
            item.alumno.contains(query, ignoreCase = true)
        }
    } else {
        data?.alumnos
    } ?: emptyList()

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        materiaSelect?.let {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = it.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${it.grupo} (${it.nivelMalla})",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Del ${it.desde} al ${it.hasta}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(Modifier.height(8.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MyFilledTonalButton(
                        text = "Generar acta de notas",
                        enabled = true,
                        icon = Icons.Filled.PictureAsPdf,
                        onClickAction = {
                            proEvaluacionesViewModel.downloadActa("acta_notas", materiaSelect!!.idMateria, homeViewModel)
                            proEvaluacionesViewModel.downloadActa("informe_acta_calificaciones", materiaSelect!!.idMateria, homeViewModel)
                        },
                        buttonColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(4.dp))

                    MyFilledTonalButton(
                        text = if (showAll) "Colapsar" else "Desplegar",
                        enabled = true,
                        icon = if (showAll) Icons.Filled.UnfoldLess else Icons.Filled.UnfoldMore,
                        onClickAction = {
                            showAll = !showAll
                        },
                        buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                        textColor = MaterialTheme.colorScheme.tertiary
                    )


                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    itemsIndexed(dataFilter) { index, calificacion ->
                        HorizontalDivider()
                        CalificacionItem(calificacion, showAll, it, proEvaluacionesViewModel)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    error?.let {
        MyErrorAlert(
            titulo = it.title,
            mensaje = it.error,
            onDismiss = {
                proEvaluacionesViewModel.clearError()
            },
            showAlert = true
        )
    }
}

@Composable
fun CalificacionItem(
    calificacion: ProEvaluacionesCalificacion,
    showAll: Boolean,
    materiaSelect: ProEvaluacionesMateria,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    val colorMap: Map<String, Map<String, Color>> = mapOf(
        "APROBADO" to mapOf(
            "container" to MaterialTheme.colorScheme.onPrimaryContainer,
            "text" to MaterialTheme.colorScheme.onPrimary
        ),
        "REPROBADO" to mapOf(
            "container" to MaterialTheme.colorScheme.errorContainer,
            "text" to MaterialTheme.colorScheme.error
        ),
        "EN CURSO" to mapOf(
            "container" to MaterialTheme.colorScheme.surfaceContainer,
            "text" to MaterialTheme.colorScheme.onSurfaceVariant
        ),
        "RECUPERACION" to mapOf(
            "container" to MaterialTheme.colorScheme.tertiaryContainer,
            "text" to MaterialTheme.colorScheme.tertiary
        ),
        "EXAMEN" to mapOf(
            "container" to MaterialTheme.colorScheme.surfaceVariant,
            "text" to MaterialTheme.colorScheme.onSurface
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = calificacion.alumno,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
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

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Text(
                    text = formatoText("Nota final:", "${calificacion.notafinal}").toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = formatoText("Asistencia:", "${calificacion.asistencia}%").toString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            MyAssistChip(
                label = calificacion.estado,
                containerColor = colorMap.get(calificacion.estado)?.get("container") ?: MaterialTheme.colorScheme.surfaceVariant,
                labelColor = colorMap.get(calificacion.estado)?.get("text") ?: MaterialTheme.colorScheme.onSurface
            )
        }

        AnimatedVisibility(
            visible = if (!showAll) expanded else showAll,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                NotasAlumno(calificacion, materiaSelect, proEvaluacionesViewModel)
            }
        }
    }
}

@Composable
fun NotasAlumno(
    calificacion: ProEvaluacionesCalificacion,
    materiaSelect: ProEvaluacionesMateria,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    val textStyle = TextStyle(
        color = if (!materiaSelect.cerrado) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(4.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val calificacionesMap = mapOf(
            "n1" to Pair("N1", "${calificacion.n1}"),
            "n2" to Pair("N2", "${calificacion.n2}"),
            "n3" to Pair("N3", "${calificacion.n3}"),
            "n4" to Pair("N4", "${calificacion.n4}"),
            "examen" to Pair("Ex", "${calificacion.examen}"),
            "total" to Pair("Total", "${calificacion.notafinal}")
        )

        calificacionesMap.forEach { (key, value) ->
            val (label, initialValue) = value
            var notaTemp by remember { mutableStateOf(initialValue) }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                MyOutlinedTextField(
                    value = notaTemp,
                    onValueChange = { notaTemp = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(70.dp),
                    enabled = when (key) {
                        "examen" -> !materiaSelect.cerrado && calificacion.estado == "EXAMEN"
                        "total" -> false
                        else -> !materiaSelect.cerrado
                    },
                    textStyle = textStyle,
                    onFocusLost = {
                        if (notaTemp != initialValue) {
                            proEvaluacionesViewModel.updateCalificacion(
                                calificacion = calificacion,
                                nota = key,
                                valor = notaTemp.toIntOrNull() ?: 0
                            )
                        }
                    }
                )
            }
            Spacer(Modifier.width(4.dp))
        }
    }
}
