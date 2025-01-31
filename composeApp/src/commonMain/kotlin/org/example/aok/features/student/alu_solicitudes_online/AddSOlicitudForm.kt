package org.example.aok.features.student.alu_solicitudes_online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.example.aok.data.network.AluSolicitudDepartamentos
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.ui.components.MyFilledTonalButton
import androidx.compose.runtime.rememberCoroutineScope as rememberCoroutineScope1
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import org.example.aok.data.network.TipoEspecieAsignatura
import org.example.aok.data.network.TipoEspecieDocente
import org.example.aok.ui.components.AnimatedShimmer
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.ShimmerGridItem
import org.example.aok.ui.components.form.FormContainer

@Composable
fun AddSolicitudForm(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    FormContainer(
        title = "Generar nueva solicitud",
        navController = navController,
        content = {
            FormScreen(
                aluSolicitudesViewModel,
                homeViewModel
            )
        },
        homeViewModel = homeViewModel
    )
}

@Composable
fun FormScreen(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    homeViewModel: HomeViewModel,
) {
    val selectedDepartamento by aluSolicitudesViewModel.selectedDepartamento.collectAsState(null)
    val solicitudes by aluSolicitudesViewModel.solicitudes.collectAsState(null)
    val observacion by aluSolicitudesViewModel.observacion.collectAsState()
    val sendFormLoading by aluSolicitudesViewModel.sendFormLoading.collectAsState(false)
    val isLoading by homeViewModel.isLoading.collectAsState(false)

    LaunchedEffect(solicitudes) {
        aluSolicitudesViewModel.onloadAddForm(homeViewModel)
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
        if (sendFormLoading) {
            AnimatedShimmer()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    solicitudes?.let { Departamentos(aluSolicitudesViewModel, it.departamentos, selectedDepartamento) }
                    Especies(aluSolicitudesViewModel, selectedDepartamento)
                    Observaciones(aluSolicitudesViewModel, observacion)
                }

                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MyFilledTonalButton(
                        text = "Generar",
                        enabled = true,
                        icon = Icons.Filled.Save,
                        onClickAction = {
                            homeViewModel.homeData.value!!.persona.idInscripcion?.let {
                                aluSolicitudesViewModel.sendSolicitud(
                                    idInscripcion = it,
                                    homeViewModel = homeViewModel
                                )
                            }
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Departamentos(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    departamentos: List<AluSolicitudDepartamentos>,
    selectedDepartamento: AluSolicitudDepartamentos?
) {
    var expandedDepartamento by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
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
            textStyle = TextStyle(fontSize = 10.sp),
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = MaterialTheme.colorScheme.surfaceContainer
//            )

        )

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Especies(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    selectedDepartamento: AluSolicitudDepartamentos?
) {
    var expandedTipoSolicitud by remember { mutableStateOf(false) }
    val selectedTipoSolicitud by aluSolicitudesViewModel.selectedTipoSolicitud.collectAsState(null)
    val solicitudes by aluSolicitudesViewModel.solicitudes.collectAsState(null)
    val fileName by aluSolicitudesViewModel.fileName.collectAsState(null)
    val bytes by aluSolicitudesViewModel.byteArray.collectAsState(null)

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
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
            textStyle = TextStyle(fontSize = 10.sp),
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = MaterialTheme.colorScheme.surfaceContainer
//            )
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

    if (if (!solicitudes?.asignaturas.isNullOrEmpty() && selectedTipoSolicitud?.relacionaDocente ?: false) {
            true
        } else {
            false
        }
    ) {
        Materias(
            aluSolicitudesViewModel = aluSolicitudesViewModel,
            materias = solicitudes?.asignaturas
        )
    }

    val coroutineScope = rememberCoroutineScope1()

    if (if (selectedTipoSolicitud?.usaArchivo ?: false) {
            true
        } else {
            false
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyFilledTonalButton(
                text = "Seleccionar archivo",
                enabled = true,
                icon = Icons.Filled.FileUpload,
                onClickAction = {
                    coroutineScope.launch() {
                        val file = FileKit.pickFile()
                        aluSolicitudesViewModel.changeFileName(file?.name ?: "file")
                        file?.readBytes()?.let { aluSolicitudesViewModel.changeByteArray(it) }
                    }
                }
            )
            fileName?.let {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = it,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Observaciones(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    observacion: String
) {
    TextField(
        value = observacion,
        onValueChange = { aluSolicitudesViewModel.onObservacionChanged(it) },
        label = { Text("Observaciones") },
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        textStyle = TextStyle(fontSize = 14.sp),
        maxLines = 5,
        singleLine = false,
        shape = RoundedCornerShape(8.dp),
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = MaterialTheme.colorScheme.surfaceContainer
//        )
    )
    Spacer(Modifier.height(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Materias(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    materias: List<TipoEspecieAsignatura>?
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMateria by aluSolicitudesViewModel.selectedMateria.collectAsState(null)

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = selectedMateria?.nombre ?: "",
            onValueChange = { },
            readOnly = true,
            label = { Text(text = "Asignatura") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            textStyle = TextStyle(fontSize = 10.sp),
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = MaterialTheme.colorScheme.surfaceContainer
//            )
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            materias!!.forEach { materia ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(
                            materia.nombre,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        aluSolicitudesViewModel.changeSelectedMateria(materia)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(8.dp))

    selectedMateria?.docentes?.let {
        Docentes(
            docentes = it,
            aluSolicitudesViewModel = aluSolicitudesViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Docentes(
    docentes: List<TipoEspecieDocente>,
    aluSolicitudesViewModel: AluSolicitudesViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedDocente by aluSolicitudesViewModel.selectedDocente.collectAsState(null)

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = selectedDocente?.nombre ?: "",
            onValueChange = { },
            readOnly = true,
            label = { Text(text = "Docente") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            textStyle = TextStyle(fontSize = 10.sp),
//            colors = TextFieldDefaults.Container(
//                colors = contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
//            )
        )

        ExposedDropdownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            docentes!!.forEach { docente ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(
                            docente.nombre,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        aluSolicitudesViewModel.changeSelectedDocente(docente)
                        expanded = false
                    }
                )
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}