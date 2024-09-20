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

class InscripcionesViewModel: ViewModel() {
    val client = createHttpClient()
    val inscripcionesService = InscripcionesService(client)

    private val _data = MutableStateFlow<Inscripciones?>(null)
    val data: StateFlow<Inscripciones?> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _pagingRange = MutableStateFlow<Int>(20)
    val pagingRange: StateFlow<Int> = _pagingRange

    fun onloadInscripciones(
        search: String,
        desde: Int = 1,
        hasta: Int = _pagingRange.value
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = inscripcionesService.fetchInscripciones(search, desde, hasta)
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
                _isLoading.value = false
            }
        }
    }
}