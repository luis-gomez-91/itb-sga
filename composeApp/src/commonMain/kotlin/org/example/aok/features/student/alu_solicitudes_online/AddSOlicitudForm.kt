package org.example.aok.features.student.alu_solicitudes_online

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyExposedDropdownMenuBox
import org.example.aok.ui.components.MyOutlinedTextFieldArea
import org.example.aok.ui.components.form.FormContainer
import org.example.aok.ui.components.shimmer.ShimmerFormLoadingAnimation

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
            ShimmerFormLoadingAnimation()
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

@Composable
fun Departamentos(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    departamentos: List<AluSolicitudDepartamentos>,
    selectedDepartamento: AluSolicitudDepartamentos?
) {
    var expandedDepartamento by remember { mutableStateOf(false) }

    MyExposedDropdownMenuBox(
        expanded = expandedDepartamento,
        onExpandedChange = { expandedDepartamento = it },
        label = "Departamento",
        selectedOption = selectedDepartamento,
        options = departamentos,
        onOptionSelected = { selectedOption ->
            aluSolicitudesViewModel.changeSelectedDepartamento(selectedOption)
            expandedDepartamento = false
        },
        getOptionDescription = { it.nombre },
        enabled = true,
        onSearchTextChange = {}
    )
    Spacer(modifier = Modifier.height(16.dp))
}

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

    selectedDepartamento?.let { departamento ->
        MyExposedDropdownMenuBox(
            expanded = expandedTipoSolicitud,
            onExpandedChange = { expandedTipoSolicitud = it },
            label = "Tipo de solicitud",
            selectedOption = selectedTipoSolicitud,
            options = departamento.especies,
            onOptionSelected = { selectedOption ->
                aluSolicitudesViewModel.changeSelectedTipoEspecie(selectedOption)
                expandedTipoSolicitud = false
            },
            getOptionDescription = { it.nombre },
            enabled = true,
            onSearchTextChange = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
    }


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
                .background(color = Color.Transparent)
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), shape = RoundedCornerShape(4.dp))
                .clip(RoundedCornerShape(4.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MyFilledTonalButton(
                text = "Seleccionar archivo",
                enabled = true,
                icon = Icons.Filled.UploadFile,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(start = 4.dp),
                onClickAction = {
                    coroutineScope.launch() {
                        val file = FileKit.pickFile()
                        aluSolicitudesViewModel.changeFileName(file?.name ?: "Archivo no seleccionado")
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

@Composable
fun Observaciones(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    observacion: String
) {
    MyOutlinedTextFieldArea(
        value = observacion,
        onValueChange = { aluSolicitudesViewModel.onObservacionChanged(it) },
        label = "Observaciones",
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
fun Materias(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    materias: List<TipoEspecieAsignatura>?
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMateria by aluSolicitudesViewModel.selectedMateria.collectAsState(null)


    MyExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        label = "Asignatura",
        selectedOption = selectedMateria,
        options = materias!!,
        onOptionSelected = { selectedOption ->
            aluSolicitudesViewModel.changeSelectedMateria(selectedOption)
            expanded = false
        },
        getOptionDescription = { it.nombre },
        enabled = true,
        onSearchTextChange = {}
    )

    Spacer(Modifier.height(16.dp))

    selectedMateria?.docentes?.let {
        Docentes(
            docentes = it,
            aluSolicitudesViewModel = aluSolicitudesViewModel
        )
    }
}

@Composable
fun Docentes(
    docentes: List<TipoEspecieDocente>,
    aluSolicitudesViewModel: AluSolicitudesViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedDocente by aluSolicitudesViewModel.selectedDocente.collectAsState(null)

    MyExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        label = "Docente",
        selectedOption = selectedDocente,
        options = docentes,
        onOptionSelected = { selectedOption ->
            aluSolicitudesViewModel.changeSelectedDocente(selectedOption)
            expanded = false
        },
        getOptionDescription = { it.nombre },
        enabled = true,
        onSearchTextChange = {}
    )

    Spacer(Modifier.height(16.dp))
}