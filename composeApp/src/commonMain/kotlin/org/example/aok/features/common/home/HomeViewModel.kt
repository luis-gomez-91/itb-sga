package org.example.aok.features.common.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.URLOpener
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Home
import org.example.aok.data.network.HomeResult
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.Report
import org.example.aok.data.network.ReportForm
import org.example.aok.data.network.ReportResult
import org.example.aok.data.network.Error

class HomeViewModel(private val pdfOpener: URLOpener) : ViewModel() {
    val client = createHttpClient()
    val service = HomeService(client)

    private val _homeData = MutableStateFlow<Home?>(null)
    val homeData: StateFlow<Home?> = _homeData

    fun onloadHome(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchHome(id)
                logInfo("home", "$result")

                when (result) {
                    is HomeResult.Success -> {
                        _homeData.value = result.home
                        clearError()
                        if (_periodoSelect.value == null) {
                            _periodoSelect.value = result.home.periodos[0]
                        }
                    }
                    is HomeResult.Failure -> {
                        _error.value = result.error
                        _error.value!!.title = "Error al cargar datos"
                    }
                    else -> {}
                }

            } catch (e: Exception) {
                addError(title = "Error", text = "${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

//  -----------------------------------------------------------  ERROR ---------------------------------------------------------
    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun addError(title: String, text: String) {
        val newError = Error(
            title = title,
            error = text
        )
        _error.value = newError
    }

//  ----------------------------------------------------------  LOADING ---------------------------------------------------------
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun changeLoading(value: Boolean) {
        _isLoading.value = value
    }

//  ----------------------------------------------------------  BUSQUEDA ---------------------------------------------------------
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _fastSearch = MutableStateFlow<Boolean>(true)
    val fastSearch: StateFlow<Boolean> get() = _fastSearch

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun changeFastSearch(estado: Boolean) {
        _fastSearch.value = estado
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

//  ----------------------------------------------------------  REPORTES ---------------------------------------------------------
    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report

    fun onloadReport(form: ReportForm) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchReport(form)
                when (result) {
                    is ReportResult.Success -> {
                        _report.value = result.report
                        _error.value = null
                        val url = "https://sga.itb.edu.ec${_report.value!!.url}"
                        openURL(url)
                    }
                    is ReportResult.Failure -> {
                        _error.value = result.error
                        _error.value!!.title = "Error al generar reporte"
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                logInfo("report", "$e")
                addError(title = "Error", text = "${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun openURL(url: String) {
        viewModelScope.launch {
            try {
                pdfOpener.openURL(url)
            } catch (e: Exception) {
                logInfo("alu_facturacion", "ERROR: ${e}")
            }
        }
    }


//  ----------------------------------------------------------  PAGINADO ---------------------------------------------------------
    private val _actualPage = MutableStateFlow<Int>(1)
    val actualPage: StateFlow<Int> = _actualPage

    fun pageMore() {
        _actualPage.value += 1
    }

    fun pageLess() {
        _actualPage.value -= 1
    }

    fun actualPageRestart() {
        _actualPage.value = 1
    }

    //  --------------------------------------------------- BOTTOM SHEET PERIODOS ---------------------------------------------------
    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    private val _periodoSelect = MutableStateFlow<Periodo?>(null)
    val periodoSelect: StateFlow<Periodo?> = _periodoSelect

    fun changeBottomSheet() {
        _showBottomSheet.value = !_showBottomSheet.value
    }

    fun changePeriodoSelect(periodo: Periodo) {
        _periodoSelect.value = periodo
    }
}



