package org.itb.sga.features.student.alu_notas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.AluNotasResult
import org.itb.sga.data.network.TipoAsignaturaNota

class AluNotasViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluNotasService(client)

    private val _data = MutableStateFlow<TipoAsignaturaNota?>(null)
    val data: StateFlow<TipoAsignaturaNota?> = _data


    private val _error = MutableStateFlow<String?>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAluNotaAsignatura(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluNota(id)
                logInfo("alu_notas", "$result")

                when (result) {
                    is AluNotasResult.Success -> {
                        _data.value = result.tipoAsignaturaNota
                        _error.value = ""
                    }
                    is AluNotasResult.Failure -> {
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