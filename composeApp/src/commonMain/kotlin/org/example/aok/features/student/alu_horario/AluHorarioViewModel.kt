package org.example.aok.features.student.alu_horario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluHorario
import org.example.aok.data.network.AluHorarioResult

class AluHorarioViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluHorarioService(client)

    private val _data = MutableStateFlow<List<AluHorario>>(emptyList())
    val data: StateFlow<List<AluHorario>> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAluHorario(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluHorario(id)
                logInfo("alu_horario", "$result")

                when (result) {
                    is AluHorarioResult.Success -> {
                        _data.value = result.aluHorario
                        _error.value = ""
                    }
                    is AluHorarioResult.Failure -> {
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