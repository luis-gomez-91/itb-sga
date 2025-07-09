package org.itb.sga.features.student.alu_solicitud_beca.ficha_socioeconomica

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.itb.sga.data.network.solicitud_becas.FichaReferenciaFamiliarData
import org.itb.sga.data.network.models.Parentesco
import org.itb.sga.features.student.alu_solicitud_beca.AluSolicitudBecaViewModel
import org.itb.sga.ui.components.MyExposedDropdownMenuBox
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField

@Composable
fun ReferenciaFamiliar(
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel
) {


    val data by aluSolicitudBecaViewModel.dataFicha.collectAsState(null)
    var enable by remember { mutableStateOf(false) }
    val referencias by aluSolicitudBecaViewModel.listParentesco.collectAsState(emptyList())

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column (
            Modifier.weight(1f)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Referencias del familiar",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (enable) {
                    IconButton(
                        onClick = {
                            aluSolicitudBecaViewModel.addListParentesco(FichaReferenciaFamiliarData(telefono = "", id = null, parentesco = null))
                        },
                        enabled = enable
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Agregar referencia"
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(referencias) { index, referencia ->
                    data?.referenciaFamiliar?.parentesco?.let {
                        AddParentesco(
                            referencia = referencia,
                            dataParentesco = it,
                            enable = enable,
                            aluSolicitudBecaViewModel = aluSolicitudBecaViewModel,
                            onDelete = { aluSolicitudBecaViewModel.removeListParentesco(index) },
                            index = index
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
                        enable = false
                        aluSolicitudBecaViewModel.removeEmptyReferencias()
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
                        enable = false
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

@Composable
fun AddParentesco(
    referencia: FichaReferenciaFamiliarData,
    dataParentesco: List<Parentesco>,
    enable: Boolean,
    aluSolicitudBecaViewModel: AluSolicitudBecaViewModel,
    onDelete: () -> Unit,
    index: Int
) {
    var expandedParentesco by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyExposedDropdownMenuBox(
            expanded = expandedParentesco,
            onExpandedChange = { expandedParentesco = it },
            label = "Parentesco",
            selectedOption = referencia.parentesco,
            options = dataParentesco,
            onOptionSelected = { selectedOption ->
                aluSolicitudBecaViewModel.updateParentescoForReferencia(index = index, parentesco = selectedOption)
            },
            getOptionDescription = { it.nombre },
            enabled = enable,
            modifier = Modifier.weight(1f),
            onSearchTextChange = {}
        )

        Spacer(modifier = Modifier.width(8.dp))

        MyOutlinedTextField(
            value = referencia.telefono,
            onValueChange = {  },
            placeholder = "Teléfono",
            label = "Teléfono",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(120.dp),
            enabled = enable
        )

        Spacer(modifier = Modifier.width(4.dp))
        IconButton(
            onClick = {
                onDelete()
            },
            enabled = enable
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Quitar referencia",
                tint = if (enable) MaterialTheme.colorScheme.error else Color.LightGray,
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    HorizontalDivider()
}
