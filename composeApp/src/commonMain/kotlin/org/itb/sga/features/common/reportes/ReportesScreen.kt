package org.itb.sga.features.common.reportes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import org.itb.sga.core.DatePickerComponent
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.reportes.Reporte
import org.itb.sga.data.network.reportes.ReporteCategoria
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyExposedDropdownMenuBox
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.MySwitch
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

@Composable
fun ReportesScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    reportesViewModel: ReportesViewModel
) {
    DashBoardScreen(
        title = "Reportes PDF",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                reportesViewModel
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    reportesViewModel: ReportesViewModel
) {
    val data by reportesViewModel.data.collectAsState(emptyList())
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val query by homeViewModel.searchQuery.collectAsState("")
    val error by reportesViewModel.error.collectAsState(null)

    LaunchedEffect(Unit) {
        homeViewModel.homeData.value!!.persona.idPersona.let {
            reportesViewModel.onloadReportes(
                idPersona = it,
                homeViewModel = homeViewModel,

            )
        }
    }

    val dataFilter = if (query.isNotEmpty()) {
        data.filter { item ->
            item.reportes.any { report ->
                report.descripcion.contains(query, ignoreCase = true)
            }
        }
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
                items(dataFilter) { categoria ->
                    CategoriaItem(categoria, reportesViewModel, homeViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

        error?.let {
            MyErrorAlert(
                titulo = it.title,
                mensaje = it.error,
                onDismiss = {
                    homeViewModel.clearError()
                },
                showAlert = true
            )
        }
    }
}

@Composable
fun CategoriaItem(
    categoria: ReporteCategoria,
    reportesViewModel: ReportesViewModel,
    homeViewModel: HomeViewModel
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
                        text = categoria.categoria,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
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
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .let {
                            if (categoria.reportes.size > 10) {
                                it.height(300.dp).verticalScroll(rememberScrollState()) // Desplazable si hay muchos elementos
                            } else {
                                it // Se ajusta automáticamente si hay pocos elementos
                            }
                        }
                ) {
                    categoria.reportes.forEach { reporte ->
                        ReporteItem(reporte, reportesViewModel, homeViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ReporteItem(
    reporte: Reporte,
    reportesViewModel: ReportesViewModel,
    homeViewModel: HomeViewModel
) {
//    val showBottomSheet by reportesViewModel.showBottomSheet.collectAsState(false)
    var showBottomSheet by remember { mutableStateOf(false) }


    HorizontalDivider()
    Spacer(Modifier.height(4.dp))
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showBottomSheet = true
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.PictureAsPdf,
            contentDescription = "Report",
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = reporte.descripcion,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(Modifier.height(4.dp))

    if (showBottomSheet) {
        ReportForm(reporte, reportesViewModel, homeViewModel) {
            showBottomSheet = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportForm(
    reporte: Reporte,
    reportesViewModel: ReportesViewModel,
    homeViewModel: HomeViewModel,
    onDismiss: () -> Unit,
) {
    val valores = remember { mutableStateMapOf<String, JsonElement>() }

    logInfo("reportes", "${reporte.parametros}")
    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column (
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = reporte.descripcion,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))

            reporte.parametros.forEach { parametro ->
                when (parametro.tipo) {
                    1, 7 -> {  // Texto y Cuadro de Texto
                        var text by remember { mutableStateOf("") }
                        MyOutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                                valores[parametro.nombre] = JsonPrimitive(it)
                            },
                            label = parametro.descripcion,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    2 -> { // Número entero
                        var text by remember { mutableStateOf("") }
                        MyOutlinedTextField(
                            value = text,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() }) {
                                    text = it
                                    valores[parametro.nombre] = JsonPrimitive(it.toIntOrNull() ?: 0)
                                }
                            },
                            label = parametro.descripcion,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    3 -> { // Número decimal
                        var text by remember { mutableStateOf("") }
                        MyOutlinedTextField(
                            value = text,
                            onValueChange = {
                                if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    text = it
                                    valores[parametro.nombre] = JsonPrimitive(it.toDoubleOrNull() ?: 0.0)
                                }
                            },
                            label = parametro.descripcion,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    4 -> { // Verdadero o Falso (Switch)
                        var checked by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = parametro.descripcion, modifier = Modifier.weight(1f))
                            MySwitch(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                    valores[parametro.nombre] = JsonPrimitive(it)
                                }
                            )
                        }
                    }
                    6 -> { // Fecha
                        var fecha by remember { mutableStateOf("") }

                        DatePickerComponent(
                            selectedDate = fecha,
                            onDateSelected = { newDate ->
                                fecha = newDate
                                valores[parametro.nombre] = JsonPrimitive(newDate)
                            },
                            label = parametro.descripcion
                        )
                    }
                    5 -> {
                        var expandedDropDown by remember { mutableStateOf(false) }
                        val djangoModelData by reportesViewModel.djangoModelData.collectAsState(emptyList())

                        MyExposedDropdownMenuBox(
                            expanded = expandedDropDown,
                            onExpandedChange = { expandedDropDown = it },
                            label = parametro.descripcion,
                            selectedOption = reportesViewModel.djangoModelSelect.collectAsState().value,
                            options = djangoModelData,
                            onOptionSelected = { selectedOption ->
                                reportesViewModel.updateDangoModelSelect(selectedOption)
                                expandedDropDown = false
                                valores[parametro.nombre] = JsonPrimitive(selectedOption.id)
                            },
                            getOptionDescription = { it.name },
                            enabled = true,
                            onSearchTextChange = { query ->
                                reportesViewModel.fetchDjangoModel(parametro.modelo!!, query)
                            }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            val form = org.itb.sga.data.network.ReportForm(
                reportName = reporte.nombre,
                params = valores
            )

            Spacer(Modifier.height(8.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyFilledTonalButton(
                    text = "Generar reporte",
                    enabled = true,
                    icon = Icons.Filled.PictureAsPdf,
                    onClickAction = {
                        homeViewModel.onloadReport(form)
                    }
                )
            }
        }
    }
}
