package org.example.aok.features.teacher.pro_clases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.ClaseX
import org.example.aok.data.network.Error
import org.example.aok.data.network.LeccionGrupoResult
import org.example.aok.data.network.ProClases
import org.example.aok.data.network.ProClasesResult
import org.example.aok.data.network.UpdateAsistenciaResult
import org.example.aok.data.network.form.UpdateAsistencia
import org.example.aok.data.network.form.VerClase
import org.example.aok.data.network.pro_clases.Asistencia
import org.example.aok.data.network.pro_clases.LeccionGrupo
import org.example.aok.features.common.home.HomeViewModel

class ProClasesViewModel: ViewModel() {
    val client = createHttpClient()
    val service = ProClasesService(client)

    private val _data = MutableStateFlow<ProClases?>(null)
    val data: StateFlow<ProClases?> = _data

    private val _leccionGrupoData = MutableStateFlow<LeccionGrupo?>(null)
    val leccionGrupoData: StateFlow<LeccionGrupo?> = _leccionGrupoData

    private val _claseSelect = MutableStateFlow<ClaseX?>(null)
    val claseSelect: StateFlow<ClaseX?> = _claseSelect

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun updateClaseSelect(newValue: ClaseX?) {
        _claseSelect.value = newValue
    }

    fun onloadProClases(search: String, page: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = homeViewModel.homeData.value?.persona?.idDocente?.let {
                    service.fetchProClases(search, page, it)
                }
                when (result) {
                    is ProClasesResult.Success -> {
                        _data.value = result.proClases
                        homeViewModel.clearError()
                    }
                    is ProClasesResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

    fun verLeccion(
        idLeccionGrupo: Int,
        navHostController: NavHostController
    ) {
        viewModelScope.launch {
            val form = VerClase(
                action = "verLeccion",
                idLeccionGrupo = idLeccionGrupo,
            )
            val response = service.verClase(client, form)
            when (response) {
                is LeccionGrupoResult.Success -> {
                    _leccionGrupoData.value = response.data.copy()
                    clearError()
                    navHostController.navigate("ver_clase")
                }
                is LeccionGrupoResult.Failure -> {
                    _error.value = response.error
                }
            }
        }
    }

    fun updateAsistencia(
        asistencia: Asistencia,
        newValue: Boolean,
        index: Int
    ) {
        viewModelScope.launch {
            try {
                val form = UpdateAsistencia(
                    action = "updateAsistencia",
                    idAsistencia = asistencia.asistenciaId,
                    value = newValue
                )
                logInfo("prueba", "${form}")
                val response = service.updateAsistencia(client, form)
                logInfo("prueba", "${response}")

                when (response) {
                    is UpdateAsistenciaResult.Success -> {
                        _leccionGrupoData.value?.let { leccionGrupo ->
                            val updatedAsistencias = leccionGrupo.asistencias.toMutableList().apply {
                                this[index] = response.data
                            }
                            _leccionGrupoData.value = leccionGrupo.copy(asistencias = updatedAsistencias)
                        }
                    }

                    is UpdateAsistenciaResult.Failure -> {
                        _error.value = response.error
                    }
                }
            } catch (e: Exception) {
                logInfo("prueba", "${e}")
            }
        }
    }
}