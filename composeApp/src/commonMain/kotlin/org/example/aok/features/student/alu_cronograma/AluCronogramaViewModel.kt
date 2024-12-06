package org.example.aok.features.student.alu_cronograma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.data.network.AluCronograma
import org.example.aok.data.network.AluCronogramaResult
import org.example.aok.data.network.Error
import org.example.aok.features.common.home.HomeViewModel

class AluCronogramaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluCronogramaService(client)

    private val _data = MutableStateFlow<List<AluCronograma>>(emptyList())
    val data: StateFlow<List<AluCronograma>> = _data

    fun onloadAluCronograma(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluCronograma(id)
                when (result) {
                    is AluCronogramaResult.Success -> {
                        _data.value = result.aluCronograma
                        homeViewModel.clearError()
                    }
                    is AluCronogramaResult.Failure -> {
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