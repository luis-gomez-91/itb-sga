package org.itb.sga.features.admin.inscripciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.InscripcionResult
import org.itb.sga.data.network.Inscripciones
import org.itb.sga.features.common.home.HomeViewModel

class InscripcionesViewModel: ViewModel() {
    val client = createHttpClient()
    val inscripcionesService = InscripcionesService(client)

    private val _data = MutableStateFlow<Inscripciones?>(null)
    val data: StateFlow<Inscripciones?> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    fun onloadInscripciones(search: String, page: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = inscripcionesService.fetchInscripciones(search, page)
                logInfo("inscripciones", "$result")

                when (result) {
                    is InscripcionResult.Success -> {
                        _data.value = result.inscripciones
                        _error.value = ""
                    }
                    is InscripcionResult.Failure -> {
                        _error.value = result.error.error
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}