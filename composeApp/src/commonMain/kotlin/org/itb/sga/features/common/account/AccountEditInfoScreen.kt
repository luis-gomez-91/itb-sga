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
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.runtime.MutableState
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
import org.itb.sga.core.LIST_ESTADO_CIVIL
import org.itb.sga.core.LIST_ETNIA
import org.itb.sga.core.LIST_SANGRE
import org.itb.sga.core.LIST_SEXO
import org.itb.sga.core.capitalizeWords
import org.itb.sga.data.network.Account
import org.itb.sga.data.network.reportes.DjangoModelItem
import org.itb.sga.ui.components.MyExposedDropdownMenuBox
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.dashboard.DashboardScreen2

@Composable
fun AccountEditInfoScreen(
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    idInscripcion: Int?
) {
    DashboardScreen2(
        content = { Screen(accountViewModel, idInscripcion) },
        title = "Actualización de datos",
        onBack = {
            navController.navigate("account")
        }
    )
}

@Composable
fun Screen(
    accountViewModel: AccountViewModel,
    idInscripcion: Int?
) {
    val data by accountViewModel.data.collectAsState(null)
    val pagerState = rememberPagerState { 4 }
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    data?.let { account ->
        var selectedSexo = remember { mutableStateOf(LIST_SEXO.find { it.id == account.idSexo }) }
        var selectedSangre = remember { mutableStateOf(LIST_SANGRE.find { it.id == account.idTipoSangre }) }
        var selectedEstadoCivil = remember { mutableStateOf(LIST_ESTADO_CIVIL.find { it.id == account.idEstadoCivil }) }
        var selectedEtnia = remember { mutableStateOf(LIST_ETNIA.find { it.id == account.idEtnia }) }

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
                        "Personal" to Icons.Filled.Contacts,
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
                        0 -> PersonalInfo(account, idInscripcion, selectedSexo, selectedSangre, selectedEstadoCivil, selectedEtnia)
                        1 -> ContactoInfo(account)
                        2 -> ResidenciaInfo(account, accountViewModel)
                        3 -> AdicionalInfo(account)
                    }
                }

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
                    icon = Icons.Filled.Save,
                    onClickAction = {
                        selectedSexo.value?.let {
                            selectedSangre.value?.let { it1 ->
                                selectedEstadoCivil.value?.let { it2 ->
                                    accountViewModel.updateAccount(
                                        it.id,
                                        it1.id,
                                        it2.id,
                                        12,12,"12", 12, 12, 12, 12, "",
                                        "", 12, "", ""
                                    )
                                }
                            }
                        }
                    },
                    iconSize = 24.dp,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    enabled = true
                )
            }
        }
    }
}

@Composable
fun PersonalInfo (
    data: Account,
    idInscripcion: Int?,
    selectedSexo: MutableState<DjangoModelItem?>,
    selectedSangre: MutableState<DjangoModelItem?>,
    selectedEstadoCivil: MutableState<DjangoModelItem?>,
    selectedEtnia: MutableState<DjangoModelItem?>
) {
    var expandedSexo by remember { mutableStateOf(false) }
    var expandedSangre by remember { mutableStateOf(false) }
    var expandedEstadoCivil by remember { mutableStateOf(false) }
    var expandedEtnia by remember { mutableStateOf(false) }

    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            DisableTextField(data.nombre, "Nombre")
            Spacer(Modifier.height(8.dp))
            DisableTextField(data.identificacion, data.tipoIdentificacion.capitalizeWords())
            Spacer(Modifier.height(8.dp))
            DisableTextField(data.username, "Usuario")
            Spacer(Modifier.height(8.dp))
            data.nacionalidad?.let { DisableTextField(it, "Nacionalidad") }
        }

        item {
            MyExposedDropdownMenuBox(
                expanded = expandedSexo,
                onExpandedChange = { expandedSexo = it },
                label = "Sexo",
                selectedOption = selectedSexo.value,
                options = LIST_SEXO,
                onOptionSelected = { selectedOption ->
                    expandedSexo = false
                    selectedSexo.value = selectedOption
                },
                getOptionDescription = { it.name },
                enabled = true,
                onSearchTextChange = { }
            )
        }

        item {
            MyExposedDropdownMenuBox(
                expanded = expandedSangre,
                onExpandedChange = { expandedSangre = it },
                label = "Tipo de sangre",
                selectedOption = selectedSangre.value,
                options = LIST_SANGRE,
                onOptionSelected = { selectedOption ->
                    expandedSangre = false
                    selectedSangre.value = selectedOption
                },
                getOptionDescription = { it.name },
                enabled = true,
                onSearchTextChange = { }
            )
        }

        item {
            MyExposedDropdownMenuBox(
                expanded = expandedEstadoCivil,
                onExpandedChange = { expandedEstadoCivil = it },
                label = "Estado civil",
                selectedOption = selectedEstadoCivil.value,
                options = LIST_ESTADO_CIVIL,
                onOptionSelected = { selectedOption ->
                    expandedEstadoCivil = false
                    selectedEstadoCivil.value = selectedOption
                },
                getOptionDescription = { it.name },
                enabled = true,
                onSearchTextChange = { }
            )
        }

        idInscripcion?.let {
            item {
                MyExposedDropdownMenuBox(
                    expanded = expandedEtnia,
                    onExpandedChange = { expandedEtnia = it },
                    label = "Etnia",
                    selectedOption = selectedEstadoCivil.value,
                    options = LIST_ETNIA,
                    onOptionSelected = { selectedOption ->
                        expandedEtnia = false
                        selectedEtnia.value = selectedOption
                    },
                    getOptionDescription = { it.name },
                    enabled = true,
                    onSearchTextChange = { }
                )
            }
        }

    }
}

@Composable
fun DisableTextField(
    value: String,
    label: String
) {
    MyOutlinedTextField(
        value = value,
        onValueChange = { },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth(),
        enabled = false
    )
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
fun ResidenciaInfo(
    data: Account,
    accountViewModel: AccountViewModel
) {
    val calle1 = remember { mutableStateOf(data.domicilioCallePrincipal ?: "") }
    val calle2 = remember { mutableStateOf(data.domicilioCalleSecundaria ?: "") }
    val numero = remember { mutableStateOf(data.domicilio_numero ?: "") }

    val provincias by accountViewModel.provincias.collectAsState(emptyList())
    val cantones by accountViewModel.cantones.collectAsState(emptyList())
    val parroquias by accountViewModel.parroquias.collectAsState(emptyList())
    val sectores by accountViewModel.sectores.collectAsState(emptyList())

    val selectedProvincia by accountViewModel.selectedProvincia.collectAsState()
    val selectedCanton by accountViewModel.selectedCanton.collectAsState()
    val selectedParroquia by accountViewModel.selectedParroquia.collectAsState()
    val selectedSector by accountViewModel.selectedSector.collectAsState()

    var expandedProvincia by remember { mutableStateOf(false) }
    var expandedCanton by remember { mutableStateOf(false) }
    var expandedParroquia by remember { mutableStateOf(false) }
    var expandedSector by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        accountViewModel.setInitialData(data)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            MyExposedDropdownMenuBox(
                expanded = expandedProvincia,
                onExpandedChange = { expandedProvincia = it },
                label = "Provincia de residencia",
                selectedOption = selectedProvincia,
                options = provincias,
                onOptionSelected = { selectedOption ->
                    expandedProvincia = false
                    accountViewModel.selectProvincia(selectedOption)
                },
                getOptionDescription = { it.name },
                enabled = true,
                onSearchTextChange = {}
            )
        }

        item {
            MyExposedDropdownMenuBox(
                expanded = expandedCanton,
                onExpandedChange = { expandedCanton = it },
                label = "Cantón de residencia",
                selectedOption = selectedCanton,
                options = cantones,
                onOptionSelected = { selectedOption ->
                    expandedCanton = false
                    accountViewModel.selectCanton(selectedOption)
                },
                getOptionDescription = { it.name },
                enabled = selectedProvincia != null,
                onSearchTextChange = {}
            )
        }

        item {
            MyExposedDropdownMenuBox(
                expanded = expandedParroquia,
                onExpandedChange = { expandedParroquia = it },
                label = "Parroquia de residencia",
                selectedOption = selectedParroquia,
                options = parroquias,
                onOptionSelected = { selectedOption ->
                    expandedParroquia = false
                    accountViewModel.selectParroquia(selectedOption)
                },
                getOptionDescription = { it.name },
                enabled = selectedCanton != null,
                onSearchTextChange = {}
            )
        }

        item {
            MyExposedDropdownMenuBox(
                expanded = expandedSector,
                onExpandedChange = { expandedSector = it },
                label = "Sector de residencia",
                selectedOption = selectedSector,
                options = sectores,
                onOptionSelected = { selectedOption ->
                    expandedSector = false
                    accountViewModel.selectSector(selectedOption)
                },
                getOptionDescription = { it.name },
                enabled = selectedParroquia != null,
                onSearchTextChange = {}
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
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