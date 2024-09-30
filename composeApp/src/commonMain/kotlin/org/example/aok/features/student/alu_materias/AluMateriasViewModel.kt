package org.example.aok.features.student.alu_materias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluMateria
import org.example.aok.data.network.AluMateriasResult

class AluMateriasViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluMateriasService(client)

    private val _data = MutableStateFlow<List<AluMateria>>(emptyList())
    val data: StateFlow<List<AluMateria>> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAluMaterias(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluMaterias(id)
                logInfo("alu_materias", "$result")

                when (result) {
                    is AluMateriasResult.Success -> {
                        _data.value = result.aluMateria
                        _error.value = ""
                    }
                    is AluMateriasResult.Failure -> {
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