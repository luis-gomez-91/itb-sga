package org.itb.sga.features.student.alu_consulta_general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.AluConsultaGeneralResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.alu_consulta_general.AluConsultaGeneral
import org.itb.sga.features.common.home.HomeViewModel

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