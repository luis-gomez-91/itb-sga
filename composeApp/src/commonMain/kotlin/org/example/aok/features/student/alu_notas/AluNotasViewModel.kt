package org.example.aok.features.student.alu_notas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluNotas
import org.example.aok.data.network.AluNotasResult
import org.example.aok.data.network.Home
import org.example.aok.data.network.OtraNotaAsignatura
import org.example.aok.data.network.TipoAsignaturaNota

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