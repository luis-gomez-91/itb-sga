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
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyExposedDropdownMenuBox
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.alerts.MyInfoAlert
import org.itb.sga.ui.components.alerts.MyWarningAlert
import org.itb.sga.ui.components.dashboard.DashboardScreen2

@Composable
fun AccountEditInfoScreen(
    navController: NavHostController,
    accountViewModel: AccountViewModel,
    homeViewModel: HomeViewModel
) {
    DashboardScreen2(
        content = { Screen(accountViewModel, homeViewModel, navController) },
        title = "Actualización de datos",
        onBack = {
            navController.navigate("account")
        }
    )
}

@Composable
fun Screen(
    accountViewModel: AccountViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController
) {
    val data by accountViewModel.data.collectAsState(null)
    val pagerState = rememberPagerState { 4 }
    val selectedTabIndex by accountViewModel.tab.collectAsState(0)
    var showWarningAlert by remember { mutableStateOf(false) }
    val response by accountViewModel.response.collectAsState(null)
    val updateAccountLoading by accountViewModel.updateAccountLoading.collectAsState(false)

    val idInscripcion = homeViewModel.homeData.value?.persona?.idInscripcion

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    data?.let { account ->
        val selectedSexo = remember { mutableStateOf(LIST_SEXO.find { it.id == account.idSexo }) }
        val selectedSangre = remember { mutableStateOf(LIST_SANGRE.find { it.id == account.idTipoSangre }) }
        val selectedEstadoCivil = remember { mutableStateOf(LIST_ESTADO_CIVIL.find { it.id == account.idEstadoCivil }) }
        val selectedEtnia = remember { mutableStateOf(LIST_ETNIA.find { it.id == account.idEtnia }) }

        val celular = remember { mutableStateOf(account.celular ?: "") }
        val convencional = remember { mutableStateOf(account.convencional ?: "") }
        val email = remember { mutableStateOf(account.email ?: "") }
        val emailInst = remember { mutableStateOf(account.emailinst) }

        val provincia = remember { mutableStateOf(account.nombreProvinciaResidencia ?: "") }
        val canton = remember { mutableStateOf(account.nombreCantonResidencia ?: "") }
        val madre = remember { mutableStateOf(account.madre ?: "") }
        val padre = remember { mutableStateOf(account.padre ?: "") }

        val selectedProvincia by accountViewModel.selectedProvincia.collectAsState()
        val selectedCanton by accountViewModel.selectedCanton.collectAsState()
        val selectedParroquia by accountViewModel.selectedParroquia.collectAsState()
        val selectedSector by accountViewModel.selectedSector.collectAsState()

        val calle1 = remember { mutableStateOf(account.domicilioCallePrincipal ?: "") }
        val calle2 = remember { mutableStateOf(account.domicilioCalleSecundaria ?: "") }
        val numero = remember { mutableStateOf(account.domicilio_numero ?: "") }

        val missingFields = mutableListOf<String>()

        if (selectedSexo.value == null) missingFields.add("Sexo")
        if (selectedSangre.value == null) missingFields.add("Tipo de sangre")
        if (selectedEstadoCivil.value == null) missingFields.add("Estado civil")
        if (selectedEtnia.value == null && idInscripcion != null) missingFields.add("Etnia")
        if (celular.value.isBlank()) missingFields.add("Teléfono celular")
        if (convencional.value.isBlank()) missingFields.add("Teléfono convencional")
        if (email.value.isBlank()) missingFields.add("Email personal")
        if (madre.value.isBlank()) missingFields.add("Nombre de madre")
        if (padre.value.isBlank()) missingFields.add("Nombre de padre")
        if (selectedProvincia == null) missingFields.add("Provincia de residencia")
        if (selectedCanton == null) missingFields.add("Cantón de residencia")
        if (selectedParroquia == null) missingFields.add("Parroquia de residencia")
        if (selectedSector == null) missingFields.add("Sector de residencia")
        if (calle1.value.isBlank()) missingFields.add("Calle principal")
        if (calle2.value.isBlank()) missingFields.add("Calle secundaria")
        if (numero.value.isBlank()) missingFields.add("Número de domicilio")

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
                                accountViewModel.updateTab(index)
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
                        1 -> ContactoInfo(celular, convencional, email, emailInst)
                        2 -> ResidenciaInfo(account, accountViewModel, calle1, calle2, numero)
                        3 -> AdicionalInfo(provincia, canton, madre, padre)
                    }
                }

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        accountViewModel.updateTab(page)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (updateAccountLoading) {
                    MyCircularProgressIndicator()
                } else {
                    MyFilledTonalButton(
                        text = "Guardar cambios",
                        buttonColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.primary,
                        icon = Icons.Filled.Save,
                        onClickAction = {
                            if (missingFields.isEmpty()) {
                                homeViewModel.homeData.value?.persona?.let {
                                    accountViewModel.updateAccount(
                                        selectedSexo.value!!.id,
                                        selectedSangre.value!!.id,
                                        selectedEstadoCivil.value!!.id,
                                        celular.value.toInt(),
                                        convencional.value.toInt(),
                                        email.value,
                                        selectedProvincia!!.id,
                                        selectedCanton!!.id,
                                        selectedParroquia!!.id,
                                        selectedSector!!.id,
                                        calle1.value,
                                        calle2.value,
                                        numero.value.toInt(),
                                        padre.value,
                                        madre.value,
                                        it.idPersona
                                    )
                                }
                            } else {
                                showWarningAlert = true
                            }
                        },
                        iconSize = 24.dp,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        enabled = true
                    )
                }
            }
        }
        MyWarningAlert(
            titulo = "Datos incompletos",
            mensaje = "Falta por completar:\n${missingFields.joinToString("\n") { "- $it" }}",
            onDismiss = { showWarningAlert = false },
            showAlert = showWarningAlert
        )
    }

    response?.let {
        MyInfoAlert(
            titulo = it.status,
            mensaje = it.message,
            onDismiss = {
                accountViewModel.updateResponse(null)
                if (it.status == "success") {
                    navController.navigate("account")
                }
            },
            showAlert = true
        )
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
    celular: MutableState<String>,
    convencional: MutableState<String>,
    email: MutableState<String>,
    emailInst: MutableState<String>
) {
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
    accountViewModel: AccountViewModel,
    calle1: MutableState<String>,
    calle2: MutableState<String>,
    numero: MutableState<String>
) {
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
    provincia: MutableState<String>,
    canton: MutableState<String>,
    madre: MutableState<String>,
    padre: MutableState<String>
) {
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