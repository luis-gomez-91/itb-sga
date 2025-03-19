package org.itb.sga.features.student.alu_matricula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.AluMatriculaResult
import org.itb.sga.data.network.Error
import org.itb.sga.features.common.home.HomeViewModel

class AluMatriculaViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluMatriculaService(client)

//    private val _data = MutableStateFlow<List<AluMateria>>(emptyList())
//    val data: StateFlow<List<AluMateria>> = _data

    fun onloadAluMatricula(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluMatricula(id)
                when (result) {
                    is AluMatriculaResult.Success -> {
                        homeViewModel.clearError()
                    }
                    is AluMatriculaResult.Failure -> {
                        homeViewModel.addError(result.error)
                        logInfo("matricula", "${result.error}")
                    }
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
                logInfo("matricula", "${e.message}")
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }
}