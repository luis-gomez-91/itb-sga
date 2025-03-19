package org.itb.sga.features.teacher.pro_clases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.ClaseX
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.LeccionGrupoResult
import org.itb.sga.data.network.ProClases
import org.itb.sga.data.network.ProClasesResult
import org.itb.sga.data.network.UpdateAsistenciaResult
import org.itb.sga.data.network.form.UpdateAsistencia
import org.itb.sga.data.network.form.VerClase
import org.itb.sga.data.network.pro_clases.Asistencia
import org.itb.sga.data.network.pro_clases.LeccionGrupo
import org.itb.sga.features.common.home.HomeViewModel

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