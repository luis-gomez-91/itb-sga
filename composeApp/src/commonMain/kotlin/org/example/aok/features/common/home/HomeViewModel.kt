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
import org.example.aok.data.network.Report
import org.example.aok.data.network.ReportForm
import org.example.aok.data.network.ReportResult

class HomeViewModel(private val pdfOpener: URLOpener) : ViewModel() {
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

    fun changeLoading() {
        _isLoading.value = !_isLoading.value
    }

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
                when (result) {
                    is ReportResult.Success -> {
                        _report.value = result.report
                        _error.value = null
                        val url = "https://sga.itb.edu.ec${_report.value!!.url}"
                        openURL(url)
                    }
                    is ReportResult.Failure -> {
                        _error.value = result.error.error?:"Error inesperado"
                    }
                }
            } catch (e: Exception) {
                logInfo("report", "$e")
                _error.value = "Error: ${e.message}"
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
}


