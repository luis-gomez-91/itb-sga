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
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.data.network.Error

class AluMateriasViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluMateriasService(client)

    private val _data = MutableStateFlow<List<AluMateria>>(emptyList())
    val data: StateFlow<List<AluMateria>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() { _error.value = null }

    fun onloadAluMaterias(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluMaterias(id)
                logInfo("alu_materias", "$result")

                when (result) {
                    is AluMateriasResult.Success -> {
                        _data.value = result.aluMateria
                        _error.value = null
                    }
                    is AluMateriasResult.Failure -> {
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