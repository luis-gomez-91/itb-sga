package org.example.aok.features.student.alu_solicitud_beca.ficha_socioeconomica

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.example.aok.core.formatoText
import org.example.aok.features.student.alu_solicitud_beca.AluSolicitudBecaViewModel
import org.example.aok.ui.components.MyExposedDropdownMenuBox
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.MyOutlinedTextField

@Composable
fun DatosPersonales(
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel,
    scope: CoroutineScope
) {
    val data by aluSolicitudBecaViewModel.dataFicha.collectAsState(null)
    val datosPersonalesForm by aluSolicitudBecaViewModel.datosPersonalesForm.collectAsState(null)
    var expandedEstadoCivil by remember { mutableStateOf(false) }

    var expandedCiudades by remember { mutableStateOf(false) }
    var enable by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        aluSolicitudBecaViewModel.cancelDatosPersonalesForm()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column (
            Modifier.weight(1f)
        ) {
            // Nombres y Edad
            data?.datosPersonales?.data?.let {
                Text(
                    text = formatoText("Nombre: ", it.nombre.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatoText("${it.tipoIdentificacion}: ", it.identificacion.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatoText("Fecha nacimiento: ", "${it.fechaNacimiento} (${it.edad} años)"),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatoText("Nacionalidad: ", it.nacionalidad.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatoText("Ciudad de nacimiento: ", it.ciudadNacimiento.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatoText("Email institucional: ", it.emailInst.toString()),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            data?.datosPersonales?.let {
                // C.I./Pasaporte y Estado Civil
                MyExposedDropdownMenuBox(
                    expanded = expandedEstadoCivil,
                    onExpandedChange = { expandedEstadoCivil = it },
                    label = "Estado civil",
                    selectedOption = datosPersonalesForm?.estadoCivil,
                    options = it.estadoCivil,
                    onOptionSelected = { selectedOption ->
                        aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                            copy(estadoCivil = selectedOption)
                        }
                    },
                    getOptionDescription = { it.descripcion },
                    enabled = enable,
                    onSearchTextChange = {}
                )
                Spacer(modifier = Modifier.height(16.dp))

                MyExposedDropdownMenuBox(
                    expanded = expandedCiudades,
                    onExpandedChange = { expandedCiudades = it },
                    label = "Ciudad de residencia",
                    selectedOption = datosPersonalesForm?.ciudadResidencia,
                    options = it.ciudades,
                    onOptionSelected = { selectedOption ->
                        aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                            copy(ciudadResidencia = selectedOption)
                        }
                    },
                    getOptionDescription = { it.descripcion },
                    enabled = enable,
                    onSearchTextChange = {}
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dirección: Calle y Número
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MyOutlinedTextField(
                        value = "${datosPersonalesForm?.calleUrbanizacion}",
                        onValueChange = {
                            aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                                copy(calleUrbanizacion = it)
                            }
                        },
                        placeholder = "Calle o Urbanización",
                        label = "Calle o Urbanización",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        enabled = enable
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    MyOutlinedTextField(
                        value = "${datosPersonalesForm?.numeroCasa}",
                        onValueChange = {
                            aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                                copy(numeroCasa = it)
                            }
                        },
                        placeholder = "Número",
                        label = "Número",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(100.dp),
                        enabled = enable
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Teléfonos y Correos
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    MyOutlinedTextField(
                        value = "${datosPersonalesForm?.convencional}",
                        onValueChange = {
                            aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                                copy(convencional = it)
                            }
                        },
                        placeholder = "Tel. Domicilio",
                        label = "Tel. Domicilio",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        enabled = enable
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    MyOutlinedTextField(
                        value = "${datosPersonalesForm?.celular}",
                        onValueChange = {
                            aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                                copy(celular = it)
                            }
                        },
                        placeholder = "Celular",
                        label = "Celular",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        enabled = enable
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                MyOutlinedTextField(
                    value = "${datosPersonalesForm?.emailPersonal}",
                    onValueChange = {
                        aluSolicitudBecaViewModel.updateDatosPersonalesForm {
                            copy(emailPersonal = it)
                        }
                    },
                    placeholder = "Email Personal",
                    label = "Email Personal",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enable
                )
            }
        }

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (enable) {
                MyFilledTonalButton(
                    text = "Cancelar",
                    enabled = enable,
                    onClickAction = {
                        aluSolicitudBecaViewModel.cancelDatosPersonalesForm()
                        enable = false
                    },
                    buttonColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.error,
                    icon = Icons.Filled.Cancel
                )
                Spacer(Modifier.width(8.dp))
                MyFilledTonalButton(
                    text = "Guardar",
                    enabled = enable,
                    onClickAction = {
                        scope.launch {
                            enable = false
                            aluSolicitudBecaViewModel.sendFichaForm()
                        }
                    },
                    buttonColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    icon = Icons.Filled.Save
                )
            } else {
                MyFilledTonalButton(
                    text = "Editar",
                    enabled = !enable,
                    onClickAction = {
                        enable = true
                    },
                    buttonColor = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.tertiary,
                    icon = Icons.Filled.Edit
                )
            }
        }

    }
}