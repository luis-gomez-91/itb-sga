package org.itb.sga.features.student.alu_solicitudes_online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.AluSolicitud
import org.itb.sga.data.network.AluSolicitudDepartamentos
import org.itb.sga.data.network.AluSolicitudes
import org.itb.sga.data.network.AluSolicitudesResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.TipoEspecie
import org.itb.sga.data.network.TipoEspecieAsignatura
import org.itb.sga.data.network.TipoEspecieDocente
import org.itb.sga.data.network.form.FileForm
import org.itb.sga.data.network.form.SolicitudEspecieForm
import org.itb.sga.features.common.home.HomeViewModel

class AluSolicitudesViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluSolicitudesService(client)

    private val _data = MutableStateFlow<List<AluSolicitud>>(emptyList())
    val data: StateFlow<List<AluSolicitud>> = _data

    fun onloadAluSolicitudes(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluSolicitudes(id)
                logInfo("alu_solicitudes", "$result")

                when (result) {
                    is AluSolicitudesResult.Success -> {
                        _data.value = result.aluSolicitudes
                        homeViewModel.clearError()
                    }
                    is AluSolicitudesResult.Failure -> {
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

//    CARGAR FORMULARIO
    private val _solicitudes = MutableStateFlow<AluSolicitudes?>(null)
    val solicitudes: StateFlow<AluSolicitudes?> = _solicitudes

    private val _selectedDepartamento = MutableStateFlow<AluSolicitudDepartamentos?>(null)
    val selectedDepartamento: StateFlow<AluSolicitudDepartamentos?> = _selectedDepartamento

    private val _selectedTipoSolicitud = MutableStateFlow<TipoEspecie?>(null)
    val selectedTipoSolicitud: StateFlow<TipoEspecie?> = _selectedTipoSolicitud

    private val _selectedMateria = MutableStateFlow<TipoEspecieAsignatura?>(null)
    val selectedMateria: StateFlow<TipoEspecieAsignatura?> = _selectedMateria

    private val _selectedDocente = MutableStateFlow<TipoEspecieDocente?>(null)
    val selectedDocente: StateFlow<TipoEspecieDocente?> = _selectedDocente

    private val _uploadFormLoading = MutableStateFlow<Boolean>(false)
    val uploadFormLoading: StateFlow<Boolean> = _uploadFormLoading

    private val _fileName = MutableStateFlow<String>("")
    val fileName: StateFlow<String> = _fileName

    private val _byteArray = MutableStateFlow<ByteArray?>(null)
    val byteArray: StateFlow<ByteArray?> = _byteArray

    fun changeFileName(name: String) {
        _fileName.value = name
    }

    fun changeByteArray(data: ByteArray) {
        _byteArray.value = data
    }

    fun changeSelectedDepartamento(departamentos: AluSolicitudDepartamentos) {
        _selectedDepartamento.value = departamentos
    }

    fun changeSelectedTipoEspecie(tipoEspecie: TipoEspecie) {
        _selectedTipoSolicitud.value = tipoEspecie
    }

    fun changeSelectedMateria(materia: TipoEspecieAsignatura) {
        _selectedMateria.value = materia
    }

    fun changeSelectedDocente(docente: TipoEspecieDocente) {
        _selectedDocente.value = docente
    }

    fun onloadAddForm(homeViewModel: HomeViewModel) {
        viewModelScope.launch {
            try {
                _uploadFormLoading.value = true
                val result = homeViewModel.homeData.value?.persona?.idInscripcion?.let {
                    service.fetchTipoEspecies(
                        it
                    )
                }
                if (result != null) {
                    _solicitudes.value = result
//                    logInfo("alu_solicitudes", "${result}")
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                _uploadFormLoading.value = false
            }
        }
    }

//    ------------------------------ ADD SOLICITUD ----------------------------------------------

    private val _observacion = MutableStateFlow<String>("")
    val observacion: StateFlow<String> = _observacion

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _sendFormLoading = MutableStateFlow<Boolean>(false)
    val sendFormLoading: StateFlow<Boolean> = _sendFormLoading

    fun onObservacionChanged(obs: String) {
        _observacion.value = obs
    }

    fun sendSolicitud(
        idInscripcion: Int,
        homeViewModel: HomeViewModel
    ) {
        viewModelScope.launch {
            _sendFormLoading.value = true
            var fileForm: FileForm? = null
            if (_byteArray.value != null) {
                val fileAsIntList = _byteArray.value!!.map { it.toUByte().toInt() }
                fileForm = FileForm(file = fileAsIntList, name = _fileName.value)
            }
            val form = SolicitudEspecieForm(
                action = "addSolicitud",
                idEspecie = _selectedTipoSolicitud.value!!.id,
                observacion = _observacion.value,
                idInscripcion = idInscripcion,
                file = fileForm,
                idAsignatura = _selectedMateria.value?.id,
                idDocente = _selectedDocente.value?.id
            )
            try {
                val result = service.addSolicitud(form)
                _response.value = result
                logInfo("solicitudes", result.message)

            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                _sendFormLoading.value = false
            }
        }
    }


}