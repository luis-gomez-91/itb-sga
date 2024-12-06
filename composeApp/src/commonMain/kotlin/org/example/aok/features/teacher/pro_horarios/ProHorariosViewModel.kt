package org.example.aok.features.teacher.pro_horarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorariosResult
import org.example.aok.features.common.home.HomeViewModel

class ProHorariosViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProHorariosService(client)

    private val _data = MutableStateFlow<List<ProHorario>>(emptyList())
    val data: StateFlow<List<ProHorario>> = _data

    fun onloadProHorarios(
        id: Int,
        homeViewModel: HomeViewModel,
        periodo: Periodo
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = service.fetchProHorarios(periodo, id)
                logInfo("pro_horarios", "$result")

                when (result) {
                    is ProHorariosResult.Success -> {
                        _data.value = result.proHorarios
                        homeViewModel.clearError()
                    }
                    is ProHorariosResult.Failure -> {
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


