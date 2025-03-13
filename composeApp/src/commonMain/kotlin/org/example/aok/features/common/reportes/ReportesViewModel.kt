package org.example.aok.features.common.reportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.ReportesResult
import org.example.aok.data.network.reportes.ReporteCategoria
import org.example.aok.features.common.home.HomeViewModel

class ReportesViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ReportesService(client)

    private val _data = MutableStateFlow<List<ReporteCategoria>>(emptyList())
    val data: StateFlow<List<ReporteCategoria>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun addError(newValue: Error) {
        _error.value = newValue
    }

    fun onloadReportes(
        id: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = service.fetchReportes(id)
                logInfo("reportes", "$result")

                when (result) {
                    is ReportesResult.Success -> {
                        _data.value = result.data
                        clearError()
                    }
                    is ReportesResult.Failure -> {
                        addError(result.error)
                    }
                }

            } catch (e: Exception) {
                addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }


}


