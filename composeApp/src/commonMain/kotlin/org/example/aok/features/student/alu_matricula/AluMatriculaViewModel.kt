package org.example.aok.features.student.alu_matricula

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluMatriculaResult
import org.example.aok.data.network.Error
import org.example.aok.features.common.home.HomeViewModel

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