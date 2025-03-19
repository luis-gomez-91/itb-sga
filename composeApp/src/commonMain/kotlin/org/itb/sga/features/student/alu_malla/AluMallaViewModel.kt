package org.itb.sga.features.student.alu_malla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.AluMalla
import org.itb.sga.data.network.AluMallaResult
import org.itb.sga.data.network.Error
import org.itb.sga.features.common.home.HomeViewModel

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
                logInfo("malla", "${result}")
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
                homeViewModel.changeLoading(false)
            }
        }
    }

}