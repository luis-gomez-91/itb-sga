package org.example.aok.features.student.pago_online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.PaymentezSDK
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.core.requestPostDispatcher
import org.example.aok.data.network.DatosFacturacion
import org.example.aok.data.network.Error
import org.example.aok.data.network.PagoOnline
import org.example.aok.data.network.PagoOnlineResult
import org.example.aok.data.network.PaymentResult
import org.example.aok.data.network.Response
import org.example.aok.data.network.RubroX
import org.example.aok.data.network.form.PagoOnlineForm
import org.example.aok.features.common.home.HomeViewModel

class PagoOnlineViewModel : ViewModel() {
    val client = createHttpClient()
    val service = PagoOnlineService(client)

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _data = MutableStateFlow<PagoOnline?>(null)
    val data: StateFlow<PagoOnline?> = _data

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _selectedRubros = MutableStateFlow<List<RubroX>>(emptyList())
    val selectedRubros: StateFlow<List<RubroX>> = _selectedRubros

    private val _total = MutableStateFlow<Double>(0.00)
    val total: StateFlow<Double> = _total

    private val _switchStates = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val switchStates: StateFlow<Map<Int, Boolean>> = _switchStates

    fun updateSwitchState(rubro: RubroX, isChecked: Boolean) {
        _switchStates.value = _switchStates.value.toMutableMap().apply {
            this[rubro.id] = isChecked
        }

        val updatedRubros = if (isChecked) {
            _selectedRubros.value + rubro
        } else {
            _selectedRubros.value.filterNot { it.id == rubro.id }
        }

        _selectedRubros.value = updatedRubros
        _total.value = updatedRubros.sumOf { it.valor }
    }

    fun onloadPagoOnline(id: Int, homeViewModel: HomeViewModel) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                when (val result = service.fetchPagoOnline(id)) {
                    is PagoOnlineResult.Success -> {
                        _data.value = result.pagoOnline
                        homeViewModel.clearError()
                    }
                    is PagoOnlineResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

    fun sendTerminos(
        idInscripcion: Int,
        valor: Double,
        datosFacturacion: DatosFacturacion,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            val form = PagoOnlineForm(
                action = "pagoOnlineTerminos",
                inscripcion = idInscripcion,
                valor = valor,
                correo = datosFacturacion.correo,
                nombre = datosFacturacion.nombre,
                ruc = datosFacturacion.cedula,
                telefono = datosFacturacion.telefono,
                direccion = datosFacturacion.direccion,
                rubros = _selectedRubros.value.map { it.id }
            )
            logInfo("prueba", "FORM: $form")

            _response.value = requestPostDispatcher(client, form)
            logInfo("prueba", "RESPUESTA: ${_response.value}")
        }

    }

//    Diferir pago
    private val _showDiferirPago = MutableStateFlow<Boolean>(false)
    val showDiferirPago: StateFlow<Boolean> = _showDiferirPago

    fun updateShowDiferirPago(value: Boolean) {
        _showDiferirPago.value = value
    }

    private val _showTerminosCondiciones = MutableStateFlow<Boolean>(false)
    val showTerminosCondiciones: StateFlow<Boolean> = _showTerminosCondiciones

    fun updateShowTerminosCondiciones(value: Boolean) {
        _showTerminosCondiciones.value = value
    }

    private val _terminosCondiciones = MutableStateFlow<Boolean>(false)
    val terminosCondiciones: StateFlow<Boolean> = _terminosCondiciones

    fun updateTerminosCondiciones(value: Boolean) {
        _terminosCondiciones.value = value
    }

//    Pago online
    private val _paymentState = MutableStateFlow<PaymentResult>(PaymentResult.Idle)
    val paymentState: StateFlow<PaymentResult> = _paymentState.asStateFlow()

//    fun processPayment(uid: String, email: String, amount: Double) {
//        viewModelScope.launch {
//            logInfo("prueba", "Procesando pago... POSII")
////            PaymentezSDK.processPayment(uid, email, amount)
//        }
//    }

    fun updatePaymentState(result: PaymentResult) {
        _paymentState.value = result
        logInfo("prueba", "ENTRO A updatePaymentState")
        when (result) {
            is PaymentResult.Success -> {
                logInfo("prueba", "Pago exitoso. ID Transacción: ${result.transactionId}")
                _selectedRubros.value = emptyList()
                _switchStates.value = emptyMap()
                _total.value = 0.00
            }
            is PaymentResult.Failure -> {
                logInfo("prueba", "Error en el pago: ${result.message}")
            }
            PaymentResult.Processing -> {
                logInfo("prueba", "Procesando pago...")
            }
            PaymentResult.Idle -> { /* No hacer nada */ }
        }
    }

    fun iniciarPago(homeViewModel: HomeViewModel) {
        viewModelScope.launch {
            val totalPagar = _total.value
            if (totalPagar <= 0) {
                logInfo("prueba", "No hay rubros seleccionados para pagar")
            } else {
                _paymentState.value = PaymentResult.Processing
                _data.value?.datosFacturacion?.let {
                    _response.value?.let { it1 ->
                        PaymentezSDK.processPayment(homeViewModel.contextProvider.getContext(), it1.message, it.correo, totalPagar)
                    }
                }
            }
        }
    }

    private val _cardsState = MutableStateFlow<Result<String>>(Result.Loading)
    val cardsState: StateFlow<Result<String>> = _cardsState

    fun getCards(uid: String) {
        // Usando un coroutine scope para realizar la operación en un hilo de fondo
        viewModelScope.launch {
            try {
                // Realizar la solicitud
                val response = PaymentezSDK.doGetRequest("https://ccapi.paymentez.com/v2/card/list?uid=$uid")
                val jsonResponse = response["RESPONSE_JSON"] ?: "{}"
                // Emitir el resultado exitoso con el JSON de la respuesta
                _cardsState.value = Result.Success(jsonResponse)
            } catch (e: Exception) {
                // En caso de error, emitir el error
                _cardsState.value = Result.Error(e)
            }
        }
    }

}

//static var PAYMENTEZ_DEV_URL = "https://ccapi-stg.paymentez.com"
//static var PAYMENTEZ_PROD_URL = "https://ccapi.paymentez.com"
//static const val RESPONSE_HTTP_CODE = "RESPONSE_HTTP_CODE"
//static const val RESPONSE_JSON = "RESPONSE_JSON"