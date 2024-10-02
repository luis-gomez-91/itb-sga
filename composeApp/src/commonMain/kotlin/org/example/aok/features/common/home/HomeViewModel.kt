package org.example.aok.features.common.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.PDFOpener
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.Home
import org.example.aok.data.network.HomeResult
import org.example.aok.data.network.PagoOnlineForm
import org.example.aok.data.network.Report
import org.example.aok.data.network.ReportForm
import org.example.aok.data.network.ReportResult

class HomeViewModel(private val pdfOpener: PDFOpener) : ViewModel() {
    val client = createHttpClient()
    val service = HomeService(client)

    private val _homeData = MutableStateFlow<Home?>(null)
    val homeData: StateFlow<Home?> = _homeData

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _fastSearch = MutableStateFlow<Boolean>(true)
    val fastSearch: StateFlow<Boolean> get() = _fastSearch

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun changeFastSearch(estado: Boolean) {
        _fastSearch.value = estado
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun onloadHome(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchHome(id)
                logInfo("home", "$result")

                when (result) {
                    is HomeResult.Success -> {
                        _homeData.value = result.home
                        _error.value = null
                    }
                    is HomeResult.Failure -> {
                        _error.value = result.error.error?:"Error inesperado"
                    }
                }

            } catch (e: Exception) {
                _error.value = "${e}"?:"Error inesperado"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onloadReport(form: ReportForm) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchReport(form)
                logInfo("report", "$result")

                when (result) {
                    is ReportResult.Success -> {
                        _report.value = result.report
                        _error.value = null
                    }
                    is ReportResult.Failure -> {
                        _error.value = result.error.error?:"Error inesperado"
                    }
                }
//                openPDF(result.url)

            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun openPDF(url: String) {
        logInfo("alu_facturacion", "${url}")
//        url = "https://sga.itb.edu.ec/media//documentos/userreports/lagomez11/factura_sri20241001_130714.pdf"
        viewModelScope.launch {
            try {
                pdfOpener.openPDF(url)
            } catch (e: Exception) {
                logInfo("alu_facturacion", "ERROR POSI: ${e}")
            }
        }
    }
}


