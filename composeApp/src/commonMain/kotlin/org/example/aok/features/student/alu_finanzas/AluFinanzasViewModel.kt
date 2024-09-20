package org.example.aok.features.student.alu_finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Rubro
import org.example.aok.data.network.AluFinanzasResult

class AluFinanzasViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluFinanzasService(client)

    private val _data = MutableStateFlow<List<Rubro>>(emptyList())
    val data: StateFlow<List<Rubro>> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAluFinanzas(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluFinanzas(id)
                logInfo("alu_finanzas", "$result")

                when (result) {
                    is AluFinanzasResult.Success -> {
                        _data.value = result.aluFinanza
                        _error.value = ""
                    }
                    is AluFinanzasResult.Failure -> {
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