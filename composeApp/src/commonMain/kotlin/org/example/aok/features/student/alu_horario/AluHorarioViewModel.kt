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
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.data.network.Error

class AluHorarioViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluHorarioService(client)

    private val _data = MutableStateFlow<List<AluHorario>>(emptyList())
    val data: StateFlow<List<AluHorario>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() { _error.value = null }

    fun onloadAluHorario(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluHorario(id)
                when (result) {
                    is AluHorarioResult.Success -> {
                        _data.value = result.aluHorario
                        _error.value = null
                    }
                    is AluHorarioResult.Failure -> {
                        _error.value = result.error
                    }
                }
            } catch (e: Exception) {
                _error.value = Error(title = "Error", error = "${e.message}")
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}