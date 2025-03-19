package org.itb.sga.features.student.alu_solicitud_beca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.core.requestPostDispatcher
import org.itb.sga.data.network.AluSolicitudBecaResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.FichaSocioeconomicaResult
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.solicitud_becas.FichaReferenciaFamiliarData
import org.itb.sga.data.network.solicitud_becas.FichaSocioeconomica
import org.itb.sga.data.network.models.Parentesco
import org.itb.sga.data.network.solicitud_becas.FichaDatosPersonalesData
import org.itb.sga.data.network.solicitud_becas.SolicitudBeca
import org.itb.sga.features.common.home.HomeViewModel

class AluSolicitudBecaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluSolicitudBecaService(client)

    private val _data = MutableStateFlow<List<SolicitudBeca>?>(null)
    val data: StateFlow<List<SolicitudBeca>?> = _data

    private val _showRequisitos = MutableStateFlow<Boolean>(false)
    val showRequisitos: StateFlow<Boolean> = _showRequisitos

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    fun changeShowRequisitos(value: Boolean) {
        _showRequisitos.value = value
    }

    fun onloadAluSolicitudBeca(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluSolicitudBeca(id)
                logInfo("becas", "$result")

                when (result) {
                    is AluSolicitudBecaResult.Success -> {
                        _data.value = result.data
                        _error.value = ""
                    }
                    is AluSolicitudBecaResult.Failure -> {
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

//    Ficha Socioeconomica
    private val _dataFicha = MutableStateFlow<FichaSocioeconomica?>(null)
    val dataFicha: StateFlow<FichaSocioeconomica?> = _dataFicha

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>("")
    val error: StateFlow<String?> = _error

    fun onloadFichaSocioeconomica(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchFichaSocioeconomica(id)
                logInfo("becas", "$result")

                when (result) {
                    is FichaSocioeconomicaResult.Success -> {
                        _dataFicha.value = result.data
                        _datosPersonalesForm.value = result.data.datosPersonales.data
                        _error.value = ""

                        result.data.referenciaFamiliar.data?.forEach {
                            addListParentesco(it)
                        }
                    }
                    is FichaSocioeconomicaResult.Failure -> {
                        _error.value = result.error.error
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun sendFichaForm() {
        _response.value = _datosPersonalesForm.value?.let { requestPostDispatcher(client, it, "beca_solicitud") }
    }

//    Datos Personales
    private val _datosPersonalesForm = MutableStateFlow<FichaDatosPersonalesData?>(null)
    val datosPersonalesForm: StateFlow<FichaDatosPersonalesData?> = _datosPersonalesForm

    fun updateDatosPersonalesForm(property: FichaDatosPersonalesData.() -> FichaDatosPersonalesData) {
        _datosPersonalesForm.value = _datosPersonalesForm.value?.let { property(it) }
    }

    fun cancelDatosPersonalesForm() {
        _datosPersonalesForm.value = _dataFicha.value?.datosPersonales?.data
    }

//    Parentesco
    private val _listParentesco = MutableStateFlow<List<FichaReferenciaFamiliarData>>(emptyList())
    val listParentesco: StateFlow<List<FichaReferenciaFamiliarData>> = _listParentesco

    fun addListParentesco(data: FichaReferenciaFamiliarData) {
        _listParentesco.value = _listParentesco.value + data
    }

    fun removeListParentesco(index: Int) {
        _listParentesco.value = _listParentesco.value.toMutableList().apply {
            removeAt(index)
        }
    }

    fun updateParentescoForReferencia(index: Int, parentesco: Parentesco) {
        _listParentesco.value = _listParentesco.value.toMutableList().apply {
            this[index] = this[index].copy(parentesco = parentesco)
        }
    }

    fun removeEmptyReferencias() {
        _listParentesco.value = _listParentesco.value.filter {
            it.parentesco != null && it.telefono.isNotBlank()
        }
    }


}