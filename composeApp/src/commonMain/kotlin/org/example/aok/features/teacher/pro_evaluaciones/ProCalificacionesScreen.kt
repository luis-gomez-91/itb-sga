package org.example.aok.features.teacher.pro_evaluaciones

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.example.aok.core.formatoText
import org.example.aok.data.network.pro_evaluaciones.ProEvaluacionesCalificacion
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.MyOutlinedTextField
import org.example.aok.ui.components.alerts.MyErrorAlert
import org.example.aok.ui.components.dashboard.DashboardScreen2

@Composable
fun ProCalificacionesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    DashboardScreen2(
        content = { Screen(proEvaluacionesViewModel, homeViewModel) },
        backScreen = "pro_evaluaciones",
        title = "Calificaciones",
        navHostController = navController
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            materiaSelect?.let {
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
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                itemsIndexed(dataFilter) { index, calificacion ->
                    HorizontalDivider()
                    CalificacionItem(calificacion, proEvaluacionesViewModel, showAll)
                    Spacer(modifier = Modifier.height(8.dp))
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
}

@Composable
fun CalificacionItem(
    calificacion: ProEvaluacionesCalificacion,
    proEvaluacionesViewModel: ProEvaluacionesViewModel,
    showAll: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

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
                style = MaterialTheme.typography.labelMedium,
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

        Row {
            MyAssistChip(
                label = calificacion.estado,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.width(4.dp))
            MyAssistChip(
                label = formatoText("NOTA FINAL: ", "${calificacion.notafinal} PUNTOS").toString(),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                labelColor = MaterialTheme.colorScheme.secondary,
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
                NotasAlumno(calificacion, proEvaluacionesViewModel)
            }
        }

    }
}

@Composable
fun NotasAlumno(
    calificacion: ProEvaluacionesCalificacion,
    proEvaluacionesViewModel: ProEvaluacionesViewModel
) {
    val textStyle = TextStyle(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        textAlign = TextAlign.Center
    )

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyOutlinedTextField(
                value = "${calificacion.n1}",
                onValueChange = {  },
                label = "N1",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(60.dp),
                enabled = true,
                textStyle = textStyle
            )
            Spacer(Modifier.width(4.dp))
            MyOutlinedTextField(
                value = "${calificacion.n2}",
                onValueChange = {  },
                label = "N2",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(60.dp),
                enabled = true,
                textStyle = textStyle
            )
            Spacer(Modifier.width(4.dp))
            MyOutlinedTextField(
                value = "${calificacion.n3}",
                onValueChange = {  },
                label = "N3",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(60.dp),
                enabled = true,
                textStyle = textStyle
            )
            Spacer(Modifier.width(4.dp))
            MyOutlinedTextField(
                value = "${calificacion.n4}",
                onValueChange = {  },
                label = "N4",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(60.dp),
                enabled = true,
                textStyle = textStyle
            )
            Spacer(Modifier.width(4.dp))
            MyOutlinedTextField(
                value = "${calificacion.notafinal}",
                onValueChange = {  },
                label = "Total",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(75.dp),
                enabled = false,
                textStyle = textStyle
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}
