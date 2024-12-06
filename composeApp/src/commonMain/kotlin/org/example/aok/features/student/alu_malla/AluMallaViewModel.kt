package org.example.aok.features.student.alu_malla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluMalla
import org.example.aok.data.network.AluMallaResult
import org.example.aok.data.network.Error
import org.example.aok.features.common.home.HomeViewModel

class AluMallaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluMallaService(client)

    private val _data = MutableStateFlow<List<AluMalla>>(emptyList())
    val data: StateFlow<List<AluMalla>> = _data

    fun onloadAluMalla(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluMalla(id)
                when (result) {
                    is AluMallaResult.Success -> {
                        _data.value = result.aluMalla
                    }
                    is AluMallaResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(true)
            }
        }
    }

}