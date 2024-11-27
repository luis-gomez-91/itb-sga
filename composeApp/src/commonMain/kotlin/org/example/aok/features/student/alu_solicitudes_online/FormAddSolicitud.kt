package org.example.aok.features.student.alu_solicitudes_online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluSolicitudDepartamentos
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyFilledTonalButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormAddSolicitud(
    aluSolicitudesViewModel: AluSolicitudesViewModel,
    homeViewModel: HomeViewModel
) {
    val selectedDepartamento by aluSolicitudesViewModel.selectedDepartamento.collectAsState(null)

    val departamentos by aluSolicitudesViewModel.departamentos.collectAsState(emptyList())



    val loading by aluSolicitudesViewModel.formLoading.collectAsState(false)
    val observacion by aluSolicitudesViewModel.observacion.collectAsState()

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

            Departamentos(aluSolicitudesViewModel, departamentos, selectedDepartamento)

            Especies(aluSolicitudesViewModel, selectedDepartamento)



            Observaciones(aluSolicitudesViewModel, observacion)

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
                            aluSolicitudesViewModel.addSolicitud(idInscripcion = it, obs = observacion)
                        }

                    }
                )
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
    logInfo("alu_solicitudes", "POOOOSI")
    logInfo("alu_solicitudes", "${departamentos}")
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
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
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
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
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
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
    Spacer(Modifier.height(8.dp))
}