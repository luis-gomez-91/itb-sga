package org.example.aok.features.student.alu_cronograma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluCronograma
import org.example.aok.data.network.AluCronogramaResult

class AluCronogramaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluCronogramaService(client)

    private val _data = MutableStateFlow<List<AluCronograma>>(emptyList())
    val data: StateFlow<List<AluCronograma>> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAluCronograma(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluCronograma(id)
                logInfo("alu_cronograma", "$result")

                when (result) {
                    is AluCronogramaResult.Success -> {
                        _data.value = result.aluCronograma
                        _error.value = ""
                    }
                    is AluCronogramaResult.Failure -> {
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