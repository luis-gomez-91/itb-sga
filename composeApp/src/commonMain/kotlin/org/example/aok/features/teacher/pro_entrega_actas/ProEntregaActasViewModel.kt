package org.example.aok.features.teacher.pro_entrega_actas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProEntregaActasResult
import org.example.aok.data.network.pro_entrega_actas.ProEntregaActas
import org.example.aok.features.common.home.HomeViewModel

class ProEntregaActasViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProEntregaActasService(client)

    private val _data = MutableStateFlow<List<ProEntregaActas>>(emptyList())
    val data: StateFlow<List<ProEntregaActas>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

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

    fun entregarActa(materia: ProEntregaActas) {
        viewModelScope.launch {

        }
    }




}


