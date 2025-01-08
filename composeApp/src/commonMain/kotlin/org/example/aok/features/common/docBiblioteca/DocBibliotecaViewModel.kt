package org.example.aok.features.common.docBiblioteca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.data.network.DocBibliotecaResult
import org.example.aok.data.network.DocBibliotecas
import org.example.aok.data.network.Error
import org.example.aok.features.common.home.HomeViewModel

class DocBibliotecaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = DocBibliotecaService(client)

    private val _data = MutableStateFlow<DocBibliotecas?>(null)
    val data: StateFlow<DocBibliotecas?> = _data

    fun onloadDocBiblioteca(search: String, page: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchDocBiblioteca(search, 1)
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