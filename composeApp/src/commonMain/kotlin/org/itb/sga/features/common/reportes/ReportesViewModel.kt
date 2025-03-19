package org.itb.sga.features.common.reportes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.DjangoModelResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.ReportesResult
import org.itb.sga.data.network.reportes.DjangoModelItem
import org.itb.sga.data.network.reportes.ReporteCategoria
import org.itb.sga.features.common.home.HomeViewModel

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
        idPersona: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = service.fetchReportes(idPersona)

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

    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    fun changeBottomSheet() {
        _showBottomSheet.value = !_showBottomSheet.value
    }
    private val _djangoModelData = MutableStateFlow<List<DjangoModelItem>>(emptyList())
    val djangoModelData: StateFlow<List<DjangoModelItem>> = _djangoModelData

    private val _djangoModelSelect = MutableStateFlow<DjangoModelItem?>(null)
    val djangoModelSelect: StateFlow<DjangoModelItem?> = _djangoModelSelect

    fun updateDangoModelSelect(model: DjangoModelItem) {
        _djangoModelSelect.value = model
    }

    fun fetchDjangoModel(
        model: String,
        query: String
    ) {
        viewModelScope.launch {
            try {
                val result = service.fetchDjangoModel(model, query)
                logInfo("prueba", "${result}")

                when (result) {
                    is DjangoModelResult.Success -> {
                        _djangoModelData.value = result.data.results
                        clearError()
                    }
                    is DjangoModelResult.Failure -> {
                        addError(result.error)
                    }
                }

            } catch (e: Exception) {
                addError(Error(title = "Error", error = "${e.message}"))
            }
        }
    }
}


