package org.itb.sga.features.student.alu_finanzas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.data.network.Rubro
import org.itb.sga.data.network.AluFinanzasResult
import org.itb.sga.features.common.home.HomeViewModel
import org.itb.sga.data.network.Error

class AluFinanzasViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluFinanzasService(client)

    private val _data = MutableStateFlow<List<Rubro>>(emptyList())
    val data: StateFlow<List<Rubro>> = _data

    fun onloadAluFinanzas(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluFinanzas(id)
                when (result) {
                    is AluFinanzasResult.Success -> {
                        _data.value = result.aluFinanza
                        homeViewModel.clearError()
                    }
                    is AluFinanzasResult.Failure -> {
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