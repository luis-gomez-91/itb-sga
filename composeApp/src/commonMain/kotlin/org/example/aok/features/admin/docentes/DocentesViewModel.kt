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
import org.example.aok.features.common.home.HomeViewModel

class DocentesViewModel: ViewModel() {
    val client = createHttpClient()
    val service = DocentesService(client)

    private val _data = MutableStateFlow<Docentes?>(null)
    val data: StateFlow<Docentes?> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    fun onloadDocentes(search: String, page: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchDocentes(search)
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
                    else -> {}
                }
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}