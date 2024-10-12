package org.example.aok.features.admin.docentes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Docentes
import org.example.aok.data.network.DocentesResult

class DocentesViewModel: ViewModel() {
    val client = createHttpClient()
    val service = DocentesService(client)

    private val _data = MutableStateFlow<Docentes?>(null)
    val data: StateFlow<Docentes?> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _pagingRange = MutableStateFlow<Int>(20)
    val pagingRange: StateFlow<Int> = _pagingRange

    fun onloadDocentes(
        search: String,
        desde: Int = 1,
        hasta: Int = _pagingRange.value
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchDocentes(search, desde, hasta)
                logInfo("docentes", "$result")

                when (result) {
                    is DocentesResult.Success -> {
                        _data.value = result.docentes
//                        _data.value!!.paging.next = _data.value!!.paging.next.from + _pagingRange.value
                        _error.value = ""
                    }
                    is DocentesResult.Failure -> {
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