package org.example.aok.features.student.alu_solicitudes_online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluSolicitud
import org.example.aok.data.network.AluSolicitudDepartamentos
import org.example.aok.data.network.AluSolicitudesResult
import org.example.aok.data.network.DatosFacturacion
import org.example.aok.data.network.TipoEspecie
import org.example.aok.data.network.form.PagoOnlineForm
import org.example.aok.data.network.form.SolicitudEspecieForm
import org.example.aok.features.common.home.HomeViewModel

class AluSolicitudesViewModel: ViewModel() {
    val client = createHttpClient()
    val service = AluSolicitudesService(client)

    private val _data = MutableStateFlow<List<AluSolicitud>>(emptyList())
    val data: StateFlow<List<AluSolicitud>> = _data

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onloadAluSolicitudes(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluSolicitudes(id)
                logInfo("alu_solicitudes", "$result")

                when (result) {
                    is AluSolicitudesResult.Success -> {
                        _data.value = result.aluSolicitudes
                        _error.value = ""
                    }
                    is AluSolicitudesResult.Failure -> {
                        _error.value = result.error.error
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
                logInfo("alu_solicitudes", "${e.message}")
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

//    CARGAR FORMULARIO
    private val _departamentos = MutableStateFlow<List<AluSolicitudDepartamentos>>(emptyList())
    val departamentos: StateFlow<List<AluSolicitudDepartamentos>> = _departamentos

    private val _showForm = MutableStateFlow<Boolean>(false)
    val showForm: StateFlow<Boolean> = _showForm

    private val _selectedDepartamento = MutableStateFlow<AluSolicitudDepartamentos?>(null)
    val selectedDepartamento: StateFlow<AluSolicitudDepartamentos?> = _selectedDepartamento

    private val _selectedTipoSolicitud = MutableStateFlow<TipoEspecie?>(null)
    val selectedTipoSolicitud: StateFlow<TipoEspecie?> = _selectedTipoSolicitud

    fun changeSelectedDepartamento(departamentos: AluSolicitudDepartamentos) {
        _selectedDepartamento.value = departamentos
    }

    fun changeSelectedTipoEspecie(tipoEspecie: TipoEspecie) {
        _selectedTipoSolicitud.value = tipoEspecie
    }

    fun changeShowForm() {
        _showForm.value = !_showForm.value
    }

    fun onloadAddForm(homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchTipoEspecies()
                _departamentos.value = result
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                logInfo("alu_solicitudes", "ERROR: ${e.message}")
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

//    ------------------------------ ADD SOLICITUD ----------------------------------------------

    private val _observacion = MutableStateFlow("")
    val observacion: StateFlow<String> = _observacion

    private val _formLoading = MutableStateFlow<Boolean>(false)
    val formLoading: StateFlow<Boolean> = _formLoading

    fun onObservacionChanged(obs: String) {
        _observacion.value = obs
    }

    fun addSolicitud(idInscripcion: Int, obs: String) {
        viewModelScope.launch {
            _formLoading.value = true
            val form = SolicitudEspecieForm(
                action = "addSolicitud",
                idEspecie = _selectedTipoSolicitud.value!!.id,
                observacion = obs,
                idInscripcion = idInscripcion
            )
            try {
                val result = service.addSolicitud(form)

            } catch (e: Exception) {
                logInfo("alu_solicitudes", "${e.message}")
                _error.value = "Error: ${e.message}"
            } finally {
                _formLoading.value = false
            }
        }
    }


}