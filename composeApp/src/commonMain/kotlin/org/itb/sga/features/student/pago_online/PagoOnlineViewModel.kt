package org.itb.sga.features.student.pago_online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.core.requestPostDispatcher
import org.itb.sga.data.domain.PayData
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.PagoOnline
import org.itb.sga.data.network.PagoOnlineResult
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.RubroX
import org.itb.sga.data.network.form.PagoOnlineForm
import org.itb.sga.features.common.home.HomeViewModel

class PagoOnlineViewModel : ViewModel() {
    val client = createHttpClient()
    val service = PagoOnlineService(client)

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _data = MutableStateFlow<PagoOnline?>(null)
    val data: StateFlow<PagoOnline?> = _data

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _total = MutableStateFlow<Double>(0.00)
    val total: StateFlow<Double> = _total

    private val _linkToPay = MutableStateFlow<String?>(null)
    val linkToPay: StateFlow<String?> = _linkToPay

    private val _selectedRubros = MutableStateFlow<List<RubroX>>(emptyList())
    val selectedRubros: StateFlow<List<RubroX>> = _selectedRubros

    private val _switchStates = MutableStateFlow<Map<RubroX, Boolean>>(emptyMap())
    val switchStates: StateFlow<Map<RubroX, Boolean>> = _switchStates

    fun updateSwitchState(rubro: RubroX, isChecked: Boolean) {
        if (isChecked) {
            val rubrosAnteriores = _data.value?.rubros!!.filter { it.id < rubro.id }
            if (rubrosAnteriores.any { !_switchStates.value[it]!! }) {
                return
            }
            _switchStates.value = _switchStates.value.toMutableMap().apply {
                this[rubro] = true
            }
            _selectedRubros.value = _selectedRubros.value + rubro
        } else {
            if (_selectedRubros.value.lastOrNull()?.id == rubro.id) {
                _switchStates.value = _switchStates.value.toMutableMap().apply {
                    this[rubro] = false
                }
                _selectedRubros.value = _selectedRubros.value.filterNot { it.id == rubro.id }
            }
        }
        _total.value = _selectedRubros.value.sumOf { it.valor }
    }

    fun onloadPagoOnline(id: Int, homeViewModel: HomeViewModel) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                when (val result = service.fetchPagoOnline(id)) {
                    is PagoOnlineResult.Success -> {
                        _switchStates.value = result.pagoOnline.rubros.associate { rubro ->
                            rubro to false
                        }
                        _data.value = result.pagoOnline
                        updatePayData { copy(email = result.pagoOnline.datosFacturacion.correo) }
                        updatePayData { copy(name = result.pagoOnline.datosFacturacion.nombre) }
                        updatePayData { copy(phone = result.pagoOnline.datosFacturacion.telefono) }
                        updatePayData { copy(ruc = result.pagoOnline.datosFacturacion.cedula) }
                        updatePayData { copy(address = result.pagoOnline.datosFacturacion.direccion) }
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

    fun onloadPaymentLink(
        idInscripcion: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            val form = PagoOnlineForm(
                action = "onloadPaymentLink",
                inscripcion = idInscripcion,
                valor = _total.value,
                correo = _payData.value.email,
                nombre = _payData.value.name,
                ruc = _payData.value.ruc,
                telefono = _payData.value.phone,
                direccion = _payData.value.address,
                rubros = _selectedRubros.value.map { it.id }
            )
            logInfo("prueba", "FORM: $form")

            val result = requestPostDispatcher(client, form)
            logInfo("prueba", "RESULT: $result")
            _response.value = result
            _linkToPay.value = result.message
//            if (result.status == "success") {
//            homeViewModel.openURL(result.message)
//            }
            logInfo("prueba", "RESPUESTA: ${_response.value}")
        }
    }

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

    private val _showPayAlert = MutableStateFlow<Boolean>(false)
    val showPayAlert: StateFlow<Boolean> = _showPayAlert

    fun updateShowPayAlert(value: Boolean) {
        _showPayAlert.value = value
    }

    private val _payData = MutableStateFlow<PayData>(
        PayData(
            "",
            "",
            "",
            "",
            ""
        )
    )
    val payData: StateFlow<PayData> = _payData

    fun updatePayData(update: PayData.() -> PayData) {
        _payData.value = _payData.value.update()
    }
}
