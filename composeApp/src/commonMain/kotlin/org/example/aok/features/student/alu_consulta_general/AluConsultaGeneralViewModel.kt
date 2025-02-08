package org.example.aok.features.student.alu_consulta_general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluConsultaGeneralResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.alu_consulta_general.AluConsultaGeneral
import org.example.aok.features.common.home.HomeViewModel

class AluConsultaGeneralViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluConsultaGeneralService(client)

    private val _data = MutableStateFlow<AluConsultaGeneral?>(null)
    val data: StateFlow<AluConsultaGeneral?> = _data


    fun onloadAluConsultaGeneral(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluConsultaGeneral(id)
                logInfo("prueba", "${result}")
                when (result) {
                    is AluConsultaGeneralResult.Success -> {
                        _data.emit(result.data)
                        homeViewModel.clearError()
                    }
                    is AluConsultaGeneralResult.Failure -> {
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