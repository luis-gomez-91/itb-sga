package org.example.aok.features.teacher.pro_evaluaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProEvaluacionesResult
import org.example.aok.data.network.pro_evaluaciones.ProEvaluaciones
import org.example.aok.data.network.pro_evaluaciones.ProEvaluacionesMateria
import org.example.aok.features.common.home.HomeViewModel

class ProEvaluacionesViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProEvaluacionesService(client)

    private val _data = MutableStateFlow<ProEvaluaciones?>(null)
    val data: StateFlow<ProEvaluaciones?> = _data

    private val _materiaSelect = MutableStateFlow<ProEvaluacionesMateria?>(null)
    val materiaSelect: StateFlow<ProEvaluacionesMateria?> = _materiaSelect

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun updateMateriaSelect(materia: ProEvaluacionesMateria?) {
        _materiaSelect.value = materia
    }

    fun addError(newValue: Error) {
        _error.value = newValue
    }

    fun onloadProEvaluaciones(
        id: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = homeViewModel.periodoSelect.value?.let {
                    service.fetchProEvaluaciones(id, it.id, materiaSelect.value?.idMateria)
                }
                logInfo("evaluaciones", "$result")

                when (result) {
                    is ProEvaluacionesResult.Success -> {
                        _data.value = result.data
                        clearError()
                    }
                    is ProEvaluacionesResult.Failure -> {
                        addError(result.error)
                    }
                    null -> TODO()
                }
            } catch (e: Exception) {
                addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }


}


