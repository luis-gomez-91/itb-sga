package org.example.aok.features.teacher.pro_horarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.ComenzarClaseResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorarioClase
import org.example.aok.data.network.ProHorariosResult
import org.example.aok.data.network.form.ComenzarClaseForm
import org.example.aok.features.common.home.HomeViewModel
import org.example.aok.features.teacher.pro_clases.ProClasesViewModel

class ProHorariosViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProHorariosService(client)

    private val _data = MutableStateFlow<List<ProHorario>>(emptyList())
    val data: StateFlow<List<ProHorario>> = _data

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun onloadProHorarios(
        id: Int,
        homeViewModel: HomeViewModel,
        periodo: Periodo
    ) {
        viewModelScope.launch {
            homeViewModel.changeLoading(true)
            try {
                val result = service.fetchProHorarios(periodo, id)

                when (result) {
                    is ProHorariosResult.Success -> {
                        _data.value = result.proHorarios
                        clearError()
                    }
                    is ProHorariosResult.Failure -> {
                        _error.value = result.error
                    }
                }

            } catch (e: Exception) {
                _error.value = (Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

    fun comenzarClase(
        clase: ProHorarioClase,
        idDocente: Int,
        navHostController: NavHostController,
        proClasesViewModel: ProClasesViewModel
    ) {
        viewModelScope.launch {
            val form = ComenzarClaseForm(
                action = "comenzarClase",
                idClase = clase.idClase,
                idProfesor = idDocente
            )
            val response = service.comenzarClase(client, form)

            when (response) {
                is ComenzarClaseResult.Success -> {
                    proClasesViewModel.updateClaseSelect(response.data.claseX)
                    proClasesViewModel.verLeccion(response.data.leccionGrupo.leccionGrupoId, navHostController)
                    clearError()
                }
                is ComenzarClaseResult.Failure -> {
                    _error.value = response.error
                }
            }
            logInfo("prueba", "${response}")
        }
    }


}


