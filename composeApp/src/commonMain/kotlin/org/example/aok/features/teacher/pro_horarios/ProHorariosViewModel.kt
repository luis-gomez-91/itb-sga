package org.example.aok.features.teacher.pro_horarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.LeccionGrupoResult
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.ProHorario
import org.example.aok.data.network.ProHorarioClase
import org.example.aok.data.network.ProHorariosResult
import org.example.aok.data.network.Response
import org.example.aok.data.network.form.ComenzarClaseForm
import org.example.aok.data.network.pro_clases.LeccionGrupo
import org.example.aok.features.common.home.HomeViewModel

class ProHorariosViewModel : ViewModel() {
    val client = createHttpClient()
    val service = ProHorariosService(client)

    private val _data = MutableStateFlow<List<ProHorario>>(emptyList())
    val data: StateFlow<List<ProHorario>> = _data

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _leccionAbierta = MutableStateFlow<LeccionGrupo?>(null)
    val leccionAbierta: StateFlow<LeccionGrupo?> = _leccionAbierta

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearResponse() {
        _response.value = null
    }

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
        idDocente: Int
    ) {
        viewModelScope.launch {
            val form = ComenzarClaseForm(
                action = "comenzarClase",
                idClase = clase.idClase,
                idProfesor = idDocente
            )
            val response = service.comenzarClase(client, form)

            when (response) {
                is LeccionGrupoResult.Success -> {
                    _leccionAbierta.value = response.data
                    clearError()
                }
                is LeccionGrupoResult.Failure -> {
                    _error.value = response.error
                }
            }
            logInfo("prueba", "${response}")
        }
    }


}


