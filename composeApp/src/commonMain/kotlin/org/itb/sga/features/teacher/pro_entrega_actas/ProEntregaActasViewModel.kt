package org.itb.sga.features.teacher.pro_entrega_actas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.ProEntregaActasResult
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.EntregarActaForm
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActas
import org.itb.sga.features.common.home.HomeViewModel

class ProEntregaActasViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProEntregaActasService(client)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _data = MutableStateFlow<List<ProEntregaActas>>(emptyList())
    val data: StateFlow<List<ProEntregaActas>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    fun clearResponse() {
        _response.value = null
    }

    fun updateLoading(newValue: Boolean) {
        _isLoading.value = newValue
    }

    fun clearError() {
        _error.value = null
    }

    fun addError(newValue: Error) {
        _error.value = newValue
    }

    fun onloadProEntregaActas(
        id: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = homeViewModel.periodoSelect.value?.let { service.fetchProEntregaActas(id, it.id) }
                logInfo("pro_entrega_actas", "$result")

                when (result) {
                    is ProEntregaActasResult.Success -> {
                        _data.value = result.data
                        clearError()
                    }
                    is ProEntregaActasResult.Failure -> {
                        addError(result.error)
                    }
                    null -> addError(Error(title = "Error", error = "Seleccione un periodo"))
                }

            } catch (e: Exception) {
                addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

    private val _materiaSelect = MutableStateFlow<ProEntregaActas?>(null)
    val materiaSelect: StateFlow<ProEntregaActas?> = _materiaSelect

    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    private val _file = MutableStateFlow<ByteArray?>(null)
    val file: StateFlow<ByteArray?> = _file

    fun updateShowBottomSheet(newValue: Boolean) {
        _showBottomSheet.value = newValue
    }

    fun updateMateriaSelect(newValue: ProEntregaActas?) {
        _materiaSelect.value = newValue
    }

    fun entregarActa(
        fileActa: ByteArray,
        nameActa: String,
        fileInforme: ByteArray,
        nameInforme: String,
        idProfesor: Int,
        observaciones: String
    ) {
        viewModelScope.launch {
            val form = _materiaSelect.value?.let {
                EntregarActaForm(
                    action = "entregarActa",
                    idMateria = it.idMateria,
                    fileActa = fileActa.map { it.toUByte().toInt() },
                    fileInforme = fileInforme.map { it.toUByte().toInt() },
                    nameActa = nameActa,
                    nameInforme = nameInforme,
                    idProfesor = idProfesor,
                    observaciones = observaciones
                )
            }

            form?.let {
                _response.value = service.sendActa(client, it)
            }
            updateLoading(false)
        }
    }








}


