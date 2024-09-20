package org.example.aok.features.student.alu_malla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluMalla
import org.example.aok.data.network.AluMallaResult

class AluMallaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluMallaService(client)

    private val _data = MutableStateFlow<List<AluMalla>>(emptyList())
    val data: StateFlow<List<AluMalla>> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAluMalla(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluMalla(id)
                logInfo("alu_malla", "$result")

                when (result) {
                    is AluMallaResult.Success -> {
                        _data.value = result.aluMalla
                        _error.value = ""
                    }
                    is AluMallaResult.Failure -> {
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