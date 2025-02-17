package org.example.aok.features.student.pago_online

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.aok.data.network.RubroX
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.common.login.LoginViewModel
import org.example.aok.ui.components.MyAssistChip
import org.example.aok.ui.components.MyCard
import org.example.aok.ui.components.MyCircularProgressIndicator
import org.example.aok.ui.components.MyFilledTonalButton
import org.example.aok.ui.components.MyOutlinedTextField
import org.example.aok.ui.components.MySwitch
import org.example.aok.ui.components.alerts.MyConfirmAlert
import org.example.aok.ui.components.alerts.MyInfoAlert
import org.example.aok.ui.components.dashboard.DashBoardScreen

@Composable
fun PagoOnlineScreen(
    navController: NavHostController,
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
                pagoOnlineViewModel,
                navController
            )
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    pagoOnlineViewModel: PagoOnlineViewModel,
    navController: NavHostController
) {
    val data by pagoOnlineViewModel.data.collectAsState(null)
    val isLoading by homeViewModel.isLoading.collectAsState(false)
    val searchQuery by homeViewModel.searchQuery.collectAsState("")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState { 2 }
    val error by homeViewModel.error.collectAsState(null)

    LaunchedEffect(Unit) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value!!.persona.idInscripcion?.let {
            pagoOnlineViewModel.onloadPagoOnline(
                it, homeViewModel
            )
        }
    }

    LaunchedEffect(data) {
        selectedTabIndex = 0
        pagerState.scrollToPage(0)
    }

    if (isLoading) {
        MyCircularProgressIndicator()
    } else {
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
                    0 -> data?.let { CardRubros(it.rubros, pagoOnlineViewModel, homeViewModel) }
                    1 -> data?.let { CardDatosFacturacion(pagoOnlineViewModel) }
                }
            }
        }

        if (error != null) {
            MyInfoAlert(
                titulo = error!!.title,
                mensaje = error!!.error,
                onDismiss = {
                    homeViewModel.clearError()
                    navController.popBackStack()
                },
                showAlert = true
            )
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
                    text = "Rubros",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTabIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.MoneyOff,
                    contentDescription = "Rubros",
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
                    text = "Datos facturación",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedTabIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Datos facturación",
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
    val switchStates by pagoOnlineViewModel.switchStates.collectAsState()
    val total:Double by pagoOnlineViewModel.total.collectAsState(0.00)
    val linkToPay by pagoOnlineViewModel.linkToPay.collectAsState(null)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth().weight(1f)
                .padding(bottom = 8.dp)
        ) {
            items(rubros) { rubro ->
                val isChecked = switchStates[rubro] ?: false
                RubroItem(
                    rubro = rubro,
                    isChecked = isChecked,
                    isEnabled = true,
                    pagoOnlineViewModel = pagoOnlineViewModel
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        AnimatedVisibility(
            visible = total > 0,
            enter = slideInVertically(
                initialOffsetY = { it }
            ) + fadeIn(
                initialAlpha = 0.3f,
                animationSpec = tween(durationMillis = 500)
            ),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                MyFilledTonalButton(
                    text = "PAGAR: $${total}",
                    enabled = total > 0,
                    onClickAction = {
                        pagoOnlineViewModel.updateShowDiferirPago(true)
                    },
                    icon = Icons.Filled.Payment,
                    iconSize = 36.dp,
                    textStyle = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    val showDiferirPago by pagoOnlineViewModel.showDiferirPago.collectAsState(false)
    val showTerminosCondiciones by pagoOnlineViewModel.showTerminosCondiciones.collectAsState(false)
    val terminosCondiciones by pagoOnlineViewModel.terminosCondiciones.collectAsState(false)
    val showPayAlert by pagoOnlineViewModel.showPayAlert.collectAsState(false)
    val payData by pagoOnlineViewModel.payData.collectAsState()

    if (showDiferirPago) {
        MyConfirmAlert(
            titulo = "¿Desea diferir pago?",
            mensaje = "",
            confirmText = "No",
            cancelText = "Si",
            onCancel = {
                pagoOnlineViewModel.updateShowDiferirPago(false)
            },
            onConfirm = {
                pagoOnlineViewModel.updateShowDiferirPago(false)
                pagoOnlineViewModel.updateShowTerminosCondiciones(true)
            },
            showAlert = true
        )
    }

    if (showTerminosCondiciones) {
        MyConfirmAlert(
            titulo = "Términos y condiciones",
            mensaje = "",
            cancelText = "Salir",
            onCancel = {
                pagoOnlineViewModel.updateShowTerminosCondiciones(false)
            },
            onConfirm = {
                pagoOnlineViewModel.updateShowTerminosCondiciones(false)
//                pagoOnlineViewModel.updateShowPayAlert(true)
                homeViewModel.homeData.value!!.persona.idInscripcion?.let {
                    pagoOnlineViewModel.onloadPaymentLink(
                        idInscripcion = it,
                        homeViewModel = homeViewModel
                    )
                }
            },
            showAlert = true,
            extra = {
                val annotatedString = buildAnnotatedString {
                    append("Acepto ")
                    pushStringAnnotation(tag = "terms", annotation = "terms_and_conditions")
                    withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle().copy(color = MaterialTheme.colorScheme.primary)) {
                        append("términos y condiciones")
                    }
                    pop()
                    append(" de la compra.")
                }

                ClickableText(
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(offset, offset)
                            .firstOrNull { it.tag == "terms" }?.let {
                                pagoOnlineViewModel.updateTerminosCondiciones(true)
                            }
                    }
                )
            }
        )
    }

//    if (showPayAlert) {
//        MyConfirmAlert(
//            titulo = "Pago con tarjeta",
//            icon = null,
//            mensaje = null,
//            confirmText = "Pagar $${total}",
//            cancelText = "Cancelar",
//            onCancel = {
//                pagoOnlineViewModel.updateShowPayAlert(false)
//            },
//            onConfirm = {
//                pagoOnlineViewModel.updateShowPayAlert(false)
//            },
//            showAlert = true,
//            extra = {
//                Column (
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    MyOutlinedTextField(
//                        value = "${payData.email}",
//                        onValueChange = { pagoOnlineViewModel.updatePayData { copy(email = it) } },
//                        label = "E-mail",
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(Modifier.height(8.dp))
//                    MyOutlinedTextField(
//                        value = "${payData.phone}",
//                        onValueChange = { pagoOnlineViewModel.updatePayData { copy(phone = it) } },
//                        label = "Celular",
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(Modifier.height(8.dp))
//                    MyOutlinedTextField(
//                        value = "${payData.cardHolderName}",
//                        onValueChange = { pagoOnlineViewModel.updatePayData { copy(cardHolderName = it) } },
//                        label = "Nombre del titular",
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(Modifier.height(8.dp))
//                    MyOutlinedTextField(
//                        value = "${payData.cardNumber}",
//                        onValueChange = { pagoOnlineViewModel.updatePayData { copy(cardNumber = it) } },
//                        label = "Número de tarjeta",
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                    Spacer(Modifier.height(8.dp))
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        MyOutlinedTextField(
//                            value = "${payData.expiryMonth}",
//                            onValueChange = { pagoOnlineViewModel.updatePayData { copy(expiryMonth = it) } },
//                            label = "MM",
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            modifier = Modifier.weight(1f)
//                        )
//                        MyOutlinedTextField(
//                            value = "${payData.expiryYear}",
//                            onValueChange = { pagoOnlineViewModel.updatePayData { copy(expiryYear = it) } },
//                            label = "YY",
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            modifier = Modifier.weight(1f)
//                        )
//                        MyOutlinedTextField(
//                            value = "${payData.cvc}",
//                            onValueChange = { pagoOnlineViewModel.updatePayData { copy(cvc = it) } },
//                            label = "CVC",
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            modifier = Modifier.weight(1f)
//                        )
//                    }
//                }
//            }
//        )
//    }

    if (terminosCondiciones) {
        TerminosCondicionesDetail(pagoOnlineViewModel)
    }

}

@Composable
fun TerminosCondicionesDetail(
    pagoOnlineViewModel: PagoOnlineViewModel
) {
    val terms = listOf(
        "I. CONDICIONES GENERALES APLICABLES AL PAGO EN LÍNEA.",
        "1. El I.S.T Bolivariano de Tecnología con domicilio en Pedro Carbo y Víctor M. Rendón, Guayaquil-Ecuador (en adelante también como 'ITB'), ofrece servicios académicos:",
        "(i) En las instalaciones del ITB, ubicadas en: Pedro Carbo y Víctor M. Rendón, Guayaquil-Ecuador, horario de atención: lunes a viernes de 8:30 a 17:00 hrs.",
        "(ii) A través de Internet, en sga.itb.edu.ec",
        "2. Al pagar directamente en ITB, las transacciones y sus efectos jurídicos se regirán por estos términos y condiciones y la legislación vigente en Ecuador.",
        "3. Al pagar por Internet en sga.itb.edu.ec, se aplicarán los términos y condiciones publicados en los Sitios Electrónicos.",
        "4. ITB puede solicitar información personal a sus clientes, quienes deben garantizar la autenticidad y actualización de los datos proporcionados.",
        "II. TÉRMINOS GENERALES APLICABLES A LOS PAGOS EN LÍNEA Y POLÍTICAS DE DEVOLUCIÓN.",
        "• Primero: El CLIENTE es responsable por la veracidad de la información ingresada en sga.itb.edu.ec.",
        "• Segundo: La organización y condiciones de los servicios académicos son responsabilidad del ITB.",
        "• Tercero: Los pagos en línea están sujetos a la verificación de la entidad bancaria en un plazo de 24 horas.",
        "• Cuarto: No hay reembolsos por errores de fechas, valores incorrectos u otras causas ajenas al ITB.",
        "El ITB no está obligado a realizar devoluciones una vez efectuado el pago."
    )

    MyInfoAlert(
        titulo = "Términos y condiciones",
        mensaje = "Términos y condiciones de compra y políticas de devolución del Instituto Tecnológico Bolivariano.",
        onDismiss = { pagoOnlineViewModel.updateTerminosCondiciones(false) },
        showAlert = true,
        extra = {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(terms) { term ->
                        Text(
                            text = term,
                            style = if (term.startsWith("I.") || term.startsWith("II.")) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
                            color = if (term.startsWith("I.") || term.startsWith("II.")) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun RubroItem(
    rubro: RubroX,
    isChecked: Boolean,
    isEnabled: Boolean,
    pagoOnlineViewModel: PagoOnlineViewModel
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

            MySwitch(
                checked = isChecked,
                modifier = Modifier.align(Alignment.CenterVertically),
                onCheckedChange = { checked ->
                    pagoOnlineViewModel.updateSwitchState(rubro, checked)
                },
                enabled = isEnabled
            )
        }
    }
}

@Composable
fun CardDatosFacturacion(
    pagoOnlineViewModel: PagoOnlineViewModel
) {
    val payData by pagoOnlineViewModel.payData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        MyOutlinedTextField(
            value = payData.name,
            onValueChange = { pagoOnlineViewModel.updatePayData { copy(name = it) } },
            label = "Nombre",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyOutlinedTextField(
            value = payData.ruc,
            onValueChange = { pagoOnlineViewModel.updatePayData { copy(ruc = it) } },
            label = "Cédula / RUC",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyOutlinedTextField(
            value = payData.email,
            onValueChange = { pagoOnlineViewModel.updatePayData { copy(email = it) } },
            label = "Correo electrónico",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyOutlinedTextField(
            value = payData.phone,
            onValueChange = { pagoOnlineViewModel.updatePayData { copy(phone = it) } },
            label = "Teléfono celular",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        MyOutlinedTextField(
            value = payData.address,
            onValueChange = { pagoOnlineViewModel.updatePayData { copy(address = it) } },
            label = "Dirección",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

