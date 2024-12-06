package org.example.aok.features.teacher.pro_clases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.ProClases
import org.example.aok.data.network.ProClasesResult
import org.example.aok.features.common.home.HomeViewModel

class ProClasesViewModel: ViewModel() {
    val client = createHttpClient()
    val service = ProClasesService(client)

    private val _data = MutableStateFlow<ProClases?>(null)
    val data: StateFlow<ProClases?> = _data

    fun onloadProClases(search: String, page: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = homeViewModel.homeData.value?.persona?.idDocente?.let {
                    service.fetchProClases(search, 2,
                        it
                    )
                }
                when (result) {
                    is ProClasesResult.Success -> {
                        _data.value = result.proClases
                        homeViewModel.clearError()
                    }
                    is ProClasesResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}