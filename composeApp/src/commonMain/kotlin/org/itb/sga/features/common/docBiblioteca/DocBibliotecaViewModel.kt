package org.itb.sga.features.common.docBiblioteca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.data.network.DocBibliotecaResult
import org.itb.sga.data.network.DocBibliotecas
import org.itb.sga.data.network.Error
import org.itb.sga.features.common.home.HomeViewModel

class DocBibliotecaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = DocBibliotecaService(client)

    private val _data = MutableStateFlow<DocBibliotecas?>(null)
    val data: StateFlow<DocBibliotecas?> = _data

    fun onloadDocBiblioteca(search: String, page: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchDocBiblioteca(search, page)
                when (result) {
                    is DocBibliotecaResult.Success -> {
                        _data.value = result.docBiblioteca
                        homeViewModel.clearError()
                    }
                    is DocBibliotecaResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}