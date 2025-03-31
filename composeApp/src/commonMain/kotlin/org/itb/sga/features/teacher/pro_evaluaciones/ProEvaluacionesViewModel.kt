package org.itb.sga.features.teacher.pro_evaluaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.ProEvaluacionesResult
import org.itb.sga.data.network.ReportForm
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluaciones
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluacionesMateria
import org.itb.sga.features.common.home.HomeViewModel

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

    fun downloadActa(reportName: String, id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val form = ReportForm(
                    reportName = reportName,
                    params = mapOf(
                        "materia" to JsonPrimitive(id)
                    )
                )
                homeViewModel.onloadReport(form)
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }


}


