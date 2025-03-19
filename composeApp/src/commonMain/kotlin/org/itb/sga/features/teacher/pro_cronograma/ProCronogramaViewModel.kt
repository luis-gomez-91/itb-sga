package org.itb.sga.features.teacher.pro_cronograma

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.ProCronogramaResult
import org.itb.sga.data.network.pro_cronograma.ProCronograma
import org.itb.sga.features.common.home.HomeViewModel

class ProCronogramaViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProCronogramaService(client)

    private val _data = MutableStateFlow<List<ProCronograma>>(emptyList())
    val data: StateFlow<List<ProCronograma>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun addError(newValue: Error) {
        _error.value = newValue
    }

    fun onloadProHorarios(
        id: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = service.fetchProCronograma(id)
                logInfo("pro_cronograma", "$result")

                when (result) {
                    is ProCronogramaResult.Success -> {
                        _data.value = result.data
                        clearError()
                    }
                    is ProCronogramaResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                }

            } catch (e: Exception) {
                addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }




}


