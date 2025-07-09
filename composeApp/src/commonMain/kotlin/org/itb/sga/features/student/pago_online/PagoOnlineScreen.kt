package org.itb.sga.features.student.pago_online

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import kotlinx.serialization.json.Json
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.RubroX
import org.itb.sga.data.network.nuvei.PaymentezResponse
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.features.common.login.LoginViewModel
import org.itb.sga.ui.components.MyAssistChip
import org.itb.sga.ui.components.MyCard
import org.itb.sga.ui.components.MyCircularProgressIndicator
import org.itb.sga.ui.components.MyFilledTonalButton
import org.itb.sga.ui.components.MyOutlinedTextField
import org.itb.sga.ui.components.MySwitch
import org.itb.sga.ui.components.alerts.MyConfirmAlert
import org.itb.sga.ui.components.alerts.MyErrorAlert
import org.itb.sga.ui.components.alerts.MyInfoAlert
import org.itb.sga.ui.components.dashboard.DashBoardScreen

inline fun <reified T> processParams(message: JsMessage): T {
    val json = Json { ignoreUnknownKeys = true }
    return try {
        json.decodeFromString<T>(message.params)
    } catch (e: Exception) {
        logInfo("prueba", "Error during deserialization: ${e.stackTraceToString()}")
        throw e
    }
}

class PaymentHandler(private val viewModel: PagoOnlineViewModel) : IJsMessageHandler {
    override fun methodName(): String { return "ConfirmarPago" }

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        try {
            val data = processParams<PaymentezResponse>(message)
            viewModel.checkPaymentStatus(data)
            callback("""{"status":"OK"}""")
        } catch (e: Exception) {
            e.printStackTrace()
            callback("""{"status":"ERROR", "message": "${e.message}"}""")
        }
    }
}

class CloseHandler(private val viewModel: PagoOnlineViewModel) : IJsMessageHandler {
    override fun methodName(): String { return "closeNuveiModal" }

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        logInfo("prueba", "ENTRO AQUI POSI")
        viewModel.clearData()
    }
}

@Composable
fun PagoOnlineScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    loginViewModel: LoginViewModel,
    pagoOnlineViewModel: PagoOnlineViewModel
) {
    val referencia by pagoOnlineViewModel.referencia.collectAsState(null)
    val showModalNuvei by pagoOnlineViewModel.showModalNuvei.collectAsState(false)

    DashBoardScreen(
        title = "Pago en línea",
        navController = navController,
        content = {
            if (showModalNuvei) {
                val htmlContent = """
                    <html>
                    <head>
                        <script src="https://cdn.paymentez.com/ccapi/sdk/payment_checkout_3.0.0.min.js"></script>
                    </head>
                    <body>
                    </body>
                    </html>
                """.trimIndent()

                val webViewState = rememberWebViewStateWithHTMLData(data = htmlContent)
                val navigator = rememberWebViewNavigator()
                val jsBridge = rememberWebViewJsBridge().apply {
                    register(PaymentHandler(pagoOnlineViewModel))
                    register(CloseHandler(pagoOnlineViewModel))
                }

                val jsScript = """
                    window.addEventListener('load', () => {
                        let respuesta = "{}";
                        let intervalId = setInterval(function() {
                            let btnClose = document.querySelector('.payment-checkout-modal__close');
                            if (btnClose) {
                                clearInterval(intervalId);
                                btnClose.addEventListener('click', function() {
                                    window.kmpJsBridge.callNative("closeNuveiModal", "{}", function () {});
                                });
                            }
                        }, 500);
                        
                        var paymentCheckout = new PaymentCheckout.modal({
                            env_mode: 'prod',
                            onOpen: function() {
                                console.log('modal open');
                            },
                            onClose: function() {
                                console.log('modal closed');
                            },
                            onResponse: function(response) {
                                console.log('modal response');
                                respuesta = JSON.stringify(response);
                                
                                window.kmpJsBridge.callNative("ConfirmarPago", respuesta, function (data) {
                                
                                });
                            }
                        });
                    
                        paymentCheckout.open({
                            reference: '$referencia'
                        });
                    
                        window.addEventListener('popstate', function() {
                            paymentCheckout.close();
                        });
                    });
                """

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val loadingState = webViewState.loadingState
                    if (loadingState is LoadingState.Loading) {
                        LinearProgressIndicator(
                            progress = { loadingState.progress },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    WebView(
                        state = webViewState,
                        modifier = Modifier.weight(1f),
                        navigator = navigator,
                        webViewJsBridge = jsBridge,
                    )

                    LaunchedEffect(webViewState.loadingState) {
                        if (webViewState.loadingState !is LoadingState.Loading) {
                            navigator.evaluateJavaScript(jsScript) { result ->
//                                logInfo("prueba", "RESULT: $result")
                            }
                        }
                    }
                }
            } else {
                Screen(
                    homeViewModel,
                    pagoOnlineViewModel
                )
            }
        },
        homeViewModel = homeViewModel,
        loginViewModel = loginViewModel
    )
}

@Composable
fun Screen(
    homeViewModel: HomeViewModel,
    pagoOnlineViewModel: PagoOnlineViewModel,
) {
    val data by pagoOnlineViewModel.data.collectAsState(null)
    val isLoading by pagoOnlineViewModel.isLoading.collectAsState(false)
    var selectedTabIndex: Int by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState { 2 }
    val error by pagoOnlineViewModel.error.collectAsState(null)
    val response by pagoOnlineViewModel.response.collectAsState(null)

    LaunchedEffect(response) {
        homeViewModel.clearSearchQuery()
        homeViewModel.homeData.value?.persona?.idInscripcion?.let {
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
    }

    error?.let {
        MyErrorAlert(
            titulo = it.title,
            mensaje = it.error,
            onDismiss = {
                pagoOnlineViewModel.setError(null)
            },
            showAlert = true
        )
    }

    response?.let {
        MyInfoAlert(
            titulo = it.status,
            mensaje = it.message,
            onDismiss = {
                pagoOnlineViewModel.setResponse(null)
            },
            showAlert = true
        )
    }
}

@Composable
fun TabRowPagoOnline(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceContainer),
        indicator = { tabPositions ->
            SecondaryIndicator(
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
                    style = MaterialTheme.typography.titleMedium,
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
                    style = MaterialTheme.typography.titleMedium,
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
                    iconSize = 24.dp,
                    textStyle = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    val showDiferirPago by pagoOnlineViewModel.showDiferirPago.collectAsState(false)
    val showTerminosCondiciones by pagoOnlineViewModel.showTerminosCondiciones.collectAsState(false)
    val showTipoDiferido by pagoOnlineViewModel.showTipoDiferido.collectAsState(false)
    val terminosCondiciones by pagoOnlineViewModel.terminosCondiciones.collectAsState(false)

    if (showDiferirPago) {
        MyConfirmAlert(
            titulo = "¿Desea diferir pago?",
            mensaje = "",
            confirmText = "No",
            cancelText = "Si",
            onCancel = {
                pagoOnlineViewModel.updateShowDiferirPago(false)
                pagoOnlineViewModel.updateShowTipoDiferido(true)
            },
            onConfirm = {
                pagoOnlineViewModel.updateShowDiferirPago(false)
                pagoOnlineViewModel.updateShowTerminosCondiciones(true)
                pagoOnlineViewModel.setDiferido(0)
            },
            showAlert = true
        )
    }

    if (showTipoDiferido) {
        MyConfirmAlert(
            titulo = "Seleccione una opción",
            mensaje = "",
            confirmText = "Sin intereses",
            cancelText = "Con intereses",
            onCancel = {
                pagoOnlineViewModel.updateShowTipoDiferido(false)
                pagoOnlineViewModel.updateShowTerminosCondiciones(true)
                pagoOnlineViewModel.setDiferido(2)
            },
            onConfirm = {
                pagoOnlineViewModel.updateShowTipoDiferido(false)
                pagoOnlineViewModel.updateShowTerminosCondiciones(true)
                pagoOnlineViewModel.setDiferido(3)
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
                homeViewModel.homeData.value?.persona?.idInscripcion?.let {
                    pagoOnlineViewModel.generateReference(
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
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$ ${rubro.valor}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.tertiary
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

