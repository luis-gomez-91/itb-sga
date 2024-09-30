package org.example.aok.features.student.pago_online

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.core.MainViewModel
import org.example.aok.core.formatoText
import org.example.aok.core.logInfo
import org.example.aok.data.network.DatosFacturacion
import org.example.aok.data.network.RubroX
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.MyOutlinedTextField
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun PagoOnlineScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    pagoOnlineViewModel: PagoOnlineViewModel
) {
    DashBoardScreen(
        title = "Pago en línea",
        navController = navController,
        content = {
            Screen(
                homeViewModel,
                pagoOnlineViewModel
            )
        },
        mainViewModel = mainViewModel,
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    pagoOnlineViewModel: PagoOnlineViewModel
) {
    val data by pagoOnlineViewModel.data.collectAsState(null)
    val isLoading by pagoOnlineViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState { 2 }


    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            pagoOnlineViewModel.onloadPagoOnline(
                it
            )
        }
    }

    LaunchedEffect(data) {
        selectedTabIndex = 0
        pagerState.scrollToPage(0)
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else if (data != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }
            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
            }

            TabRowPagoOnline(
                selectedTabIndex = selectedTabIndex,
                pagoOnlineViewModel = pagoOnlineViewModel,
                onTabSelected = { index ->
                    selectedTabIndex = index
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
            ) { page ->
                when(page) {
                    0 -> CardDatosFacturacion(data!!.datosFacturacion, pagoOnlineViewModel)
                    1 -> CardRubros(data!!.rubros, pagoOnlineViewModel, homeViewModel)
                }
            }

        }
    }
}

@Composable
fun TabRowPagoOnline(
    selectedTabIndex: Int,
    pagoOnlineViewModel: PagoOnlineViewModel,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        Tab(
            selected = selectedTabIndex == 1,
            onClick = {
                onTabSelected(0)
            },
            text = {
                Text(
                    text = "Datos facturación",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTabIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Datos facturación",
                    tint = if (selectedTabIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                )
            }
        )

        Tab(
            selected = selectedTabIndex == 0,
            onClick = {
                onTabSelected(1)
            },
            text = {
                Text(
                    text = "Rubros",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTabIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.MoneyOff,
                    contentDescription = "Rubros",
                    tint = if (selectedTabIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                )
            }
        )
    }
}

@Composable
fun CardRubros(
    rubros: List<RubroX>,
    pagoOnlineViewModel: PagoOnlineViewModel,
    homeViewModel: HomeViewModel
) {
    LaunchedEffect(Unit) {
        pagoOnlineViewModel.initializeSwitchStates(rubros.size)
    }
//    val switchStates = remember { mutableStateListOf(*Array(rubros.size) { index -> index == 0 }) }
    val switchStates by pagoOnlineViewModel.switchStates.collectAsState()
    val checkedStates = remember { mutableStateListOf(*Array(rubros.size) { false }) }
    val total by pagoOnlineViewModel.total.collectAsState(0.0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(rubros.size) { index ->
                RubroItem(
                    rubro = rubros[index],
                    isChecked = checkedStates[index],
                    isEnabled = switchStates.getOrElse(index) { false },
                    onCheckedChange = { isChecked ->
                        val updatedSwitchStates = switchStates.toMutableList()
                        val updatedCheckedStates = checkedStates.toMutableList()

                        // Actualizar el estado del switch actual
                        updatedCheckedStates[index] = isChecked

                        if (isChecked) {
                            try {
                                pagoOnlineViewModel.updateSwitchState(index + 1, true)
                            } catch (e: Exception) {
                                logInfo("pago_online", e.toString())
                            }
                            try {
                                pagoOnlineViewModel.updateSwitchState(index - 1, false)
                            } catch (e: Exception) {
                                logInfo("pago_online", e.toString())
                            }
                            pagoOnlineViewModel.addRubro(rubros[index].valor)
                        } else {
                            try {
                                pagoOnlineViewModel.updateSwitchState(index + 1, false)
                            } catch (e: Exception) {
                                logInfo("pago_online", e.toString())
                            }
                            try {
                                pagoOnlineViewModel.updateSwitchState(index - 1, true)
                            } catch (e: Exception) {
                                logInfo("pago_online", e.toString())
                            }
                            pagoOnlineViewModel.removeRubro(index)

                            // Deshabilitar y deseleccionar todos los switches siguientes
//                            for (i in index  until rubros.size) {
//                                if (i != index) {
//                                    updatedSwitchStates[i] = false
//                                }
//                                updatedCheckedStates[i] = false
//                                pagoOnlineViewModel.removeRubro(i)
//                            }
                        }

                        logInfo("pago_online", updatedSwitchStates.toString())
                        logInfo("pago_online", updatedCheckedStates.toString())
                        logInfo("pago_online", "TOTAL: ${total}")

                        checkedStates.clear()
                        checkedStates.addAll(updatedCheckedStates)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            MyFilledTonalButton(
                text = "PAGAR: ${total}",
                enabled = true,
                onClickAction = {
                    homeViewModel.homeData.value!!.persona.idInscripcion?.let {
                        pagoOnlineViewModel.sendPagoOnline(
                            idInscripcion = it,
                            valor = total,
                            datosFacturacion = pagoOnlineViewModel.data.value!!.datosFacturacion
                        )
                    }
                }
            )
        }

    }
}



@Composable
fun RubroItem(
    rubro: RubroX,
    isChecked: Boolean,
    isEnabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    MyCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = rubro.nombre,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$ ${rubro.valor}",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.width(8.dp))
                    MyAssistChip(
                        label = rubro.fecha,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.secondary,
                        icon = Icons.Filled.DateRange
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = {
                    onCheckedChange(it)
                },
                enabled = isEnabled,
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant,
                )
            )
        }
    }
}


@Composable
fun CardDatosFacturacion(
    datosFacturacion: DatosFacturacion,
    pagoOnlineViewModel: PagoOnlineViewModel
) {
    val datos = remember { mutableStateOf(datosFacturacion) }

    MyCard {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            MyOutlinedTextField(
                value = datos.value.nombre,
                onValueChange = { datos.value = datos.value.copy(nombre = it) },
                placeholder = "Nombre",
                label = "Nombre",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyOutlinedTextField(
                value = datos.value.cedula,
                onValueChange = { datos.value = datos.value.copy(cedula = it) },
                placeholder = "RUC",
                label = "RUC",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyOutlinedTextField(
                value = datos.value.correo,
                onValueChange = { datos.value = datos.value.copy(correo = it) },
                placeholder = "Correo electrónico",
                label = "Correo electrónico",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            MyOutlinedTextField(
                value = datos.value.telefono,
                onValueChange = { datos.value = datos.value.copy(telefono = it) },
                placeholder = "Teléfono celular",
                label = "Teléfono celular",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()

            )
        }
    }

}

