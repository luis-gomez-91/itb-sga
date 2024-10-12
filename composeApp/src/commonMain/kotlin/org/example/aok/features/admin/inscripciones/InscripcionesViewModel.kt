package org.example.aok.features.admin.inscripciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.InscripcionResult
import org.example.aok.data.network.Inscripciones
import org.example.aok.features.common.home.HomeViewModel

class InscripcionesViewModel: ViewModel() {
    val client = createHttpClient()
    val inscripcionesService = InscripcionesService(client)

    private val _data = MutableStateFlow<Inscripciones?>(null)
    val data: StateFlow<Inscripciones?> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

//    private val _isLoading = MutableStateFlow<Boolean>(false)
//    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadInscripciones(search: String, page: Int, homeViewModel: HomeViewModel) {
//        _isLoading.value = true
        homeViewModel.changeLoading()
        viewModelScope.launch {
            try {
                val result = inscripcionesService.fetchInscripciones(search, page)
                logInfo("inscripciones", "$result")

                when (result) {
                    is InscripcionResult.Success -> {
                        _data.value = result.inscripciones
//                        _data.value!!.paging.next = _data.value!!.paging.next.from + _pagingRange.value
                        _error.value = ""
                    }
                    is InscripcionResult.Failure -> {
                        _error.value = result.error.error ?: "An unknown error occurred"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
            } finally {
//                _isLoading.value = false
                homeViewModel.changeLoading()
            }
        }
    }
}