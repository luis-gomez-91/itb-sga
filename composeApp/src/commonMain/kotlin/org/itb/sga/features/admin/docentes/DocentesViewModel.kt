package org.itb.sga.features.admin.docentes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.data.network.Docentes
import org.itb.sga.data.network.DocentesResult
import org.itb.sga.features.common.home.HomeViewModel

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

                when (result) {
                    is DocentesResult.Success -> {
                        _data.value = result.docentes
                        _error.value = ""
                    }
                    is DocentesResult.Failure -> {
                        _error.value = result.error.error
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}