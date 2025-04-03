package org.itb.sga.features.common.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.serialization.json.JsonPrimitive
import org.itb.sga.data.network.Account
import org.itb.sga.features.common.reportes.ReportesViewModel
import org.itb.sga.ui.components.MyExposedDropdownMenuBox
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.dashboard.DashboardScreen2

@Composable
fun AccountEditInfoScreen(
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    reportesViewModel: ReportesViewModel
) {
    DashboardScreen2(
        content = { Screen(accountViewModel, reportesViewModel) },
        title = "Actualización de datos",
        onBack = {
            navController.navigate("account")
        }
    )
}

@Composable
fun Screen(
    accountViewModel: AccountViewModel,
    reportesViewModel: ReportesViewModel
) {
    val data by accountViewModel.data.collectAsState(null)
    val pagerState = rememberPagerState { 3 }
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Sincroniza el cambio de datos con el índice del tab y la animación de la página
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    data?.let { account ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Los tabs estarán dentro de una función composable válida
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                .height(3.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    listOf(
                        "Contacto" to Icons.Filled.Contacts,
                        "Residencia" to Icons.Filled.MyLocation,
                        "Adicional" to Icons.Filled.Info
                    ).forEachIndexed { index, pair ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                selectedTabIndex = index // Cambia el índice seleccionado
                            },
                            text = {
                                Text(
                                    text = pair.first,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = pair.second,
                                    contentDescription = pair.first,
                                    tint = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    when (page) {
                        0 -> ContactoInfo(account)
                        1 -> ResidenciaInfo(account, reportesViewModel)
                        2 -> AdicionalInfo(account)
                    }
                }

                // Sincroniza `selectedTabIndex` cuando cambia el pagerState
                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        selectedTabIndex = page
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MyFilledTonalButton(
                    text = "Enviar",
                    buttonColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.Filled.Send,
                    onClickAction = { },
                    iconSize = 32.dp,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    enabled = false
                )
            }
        }
    }
}



@Composable
fun ContactoInfo (
    data: Account
) {
    val celular = remember { mutableStateOf(data.celular ?: "") }
    val convencional = remember { mutableStateOf(data.convencional ?: "") }
    val email = remember { mutableStateOf(data.email ?: "") }
    val emailInst = remember { mutableStateOf(data.emailinst ?: "") }

    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MyOutlinedTextField(
                value = celular.value,
                onValueChange = { celular.value = it },
                label = "Teléfono Celular",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            MyOutlinedTextField(
                value = convencional.value,
                onValueChange = { convencional.value = it },
                label = "Teléfono Convencional",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            MyOutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = "Correo personal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            MyOutlinedTextField(
                value = emailInst.value,
                onValueChange = { emailInst.value = it },
                label = "Correo institucional",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
        }
    }
}

@Composable
fun ResidenciaInfo (
    data: Account,
    reportesViewModel: ReportesViewModel
) {
    val provincia = remember { mutableStateOf(JsonPrimitive("")) }
    val canton = remember { mutableStateOf(data.nombreCantonResidencia ?: "") }
    val parroquia = remember { mutableStateOf(data.nombreParroquia ?: "") }
    val sector = remember { mutableStateOf(data.sector ?: "") }
    val calle1 = remember { mutableStateOf(data.domicilioCallePrincipal ?: "") }
    val calle2 = remember { mutableStateOf(data.domicilioCalleSecundaria ?: "") }
    val numero = remember { mutableStateOf(data.domicilio_numero ?: "") }

    var expandedDropDown by remember { mutableStateOf(false) }
    val djangoModelData by reportesViewModel.djangoModelData.collectAsState(emptyList())


    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MyExposedDropdownMenuBox(
                expanded = expandedDropDown,
                onExpandedChange = { expandedDropDown = it },
                label = "Provincia de residencia",
                selectedOption = reportesViewModel.djangoModelSelect.value,
                options = djangoModelData,
                onOptionSelected = { selectedOption ->
                    reportesViewModel.updateDangoModelSelect(selectedOption) // Actualiza el estado en el ViewModel
                    expandedDropDown = false // Cierra el Dropdown
                    // Asumimos que `selectedOption` tiene un `id` y `name` que quieres almacenar
                    provincia.value = JsonPrimitive(selectedOption.id) // Almacena el ID de la provincia
                },
                getOptionDescription = { it.name }, // Proporciona la descripción de cada opción en el Dropdown
                enabled = true, // Asegura que el Dropdown esté habilitado
                onSearchTextChange = { query ->
                    // Filtra las opciones basadas en el texto de búsqueda
                    reportesViewModel.fetchDjangoModel("Provincia", query)
                }
            )
        }


        item {
            MyOutlinedTextField(
                value = calle1.value,
                onValueChange = { calle1.value = it },
                label = "Calle principal",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }

        item {
            MyOutlinedTextField(
                value = calle2.value,
                onValueChange = { calle2.value = it },
                label = "Calle secundaria",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }

        item {
            MyOutlinedTextField(
                value = numero.value,
                onValueChange = { numero.value = it },
                label = "Número de domicilio",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }
    }
}

@Composable
fun AdicionalInfo(
    data: Account
) {
    val provincia = remember { mutableStateOf(data.nombreProvinciaResidencia ?: "") }
    val canton = remember { mutableStateOf(data.nombreCantonResidencia ?: "") }
    val madre = remember { mutableStateOf(data.madre ?: "") }
    val padre = remember { mutableStateOf(data.padre ?: "") }

    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MyOutlinedTextField(
                value = provincia.value,
                onValueChange = { provincia.value = it },
                label = "Provincia de nacimiento",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
        }

        item {
            MyOutlinedTextField(
                value = canton.value,
                onValueChange = { canton.value = it },
                label = "Cantón de nacimiento",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )
        }

        item {
            MyOutlinedTextField(
                value = madre.value,
                onValueChange = { madre.value = it },
                label = "Nombre de la madre",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }

        item {
            MyOutlinedTextField(
                value = padre.value,
                onValueChange = { padre.value = it },
                label = "Nombre del padre",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth(),
                enabled = true
            )
        }
    }
}