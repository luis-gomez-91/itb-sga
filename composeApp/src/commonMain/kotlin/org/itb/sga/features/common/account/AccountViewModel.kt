package org.itb.sga.features.common.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.network.Account
import org.itb.sga.data.network.AccountResult
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.AccountForm
import org.itb.sga.data.network.reportes.DjangoModelItem
import org.itb.sga.features.common.home.HomeViewModel

class AccountViewModel: ViewModel() {
    val client = createHttpClient()
    val accountService = AccountService(client)

    private val _data = MutableStateFlow<Account?>(null)
    val data: StateFlow<Account?> = _data

    private val _tab = MutableStateFlow(0)
    val tab: StateFlow<Int> = _tab

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    fun updateResponse(newValue: Response?) {
        _response.value = newValue
    }

    fun updateTab(newValue: Int) {
        _tab.value = newValue
    }

    fun onloadAccount(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = accountService.fetchAccount(id)
                when (result) {
                    is AccountResult.Success -> {
                        _data.value = result.account
                        homeViewModel.clearError()
                    }
                    is AccountResult.Failure -> {
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

    private val _provincias = MutableStateFlow<List<DjangoModelItem>>(emptyList())
    val provincias: StateFlow<List<DjangoModelItem>> = _provincias

    private val _cantones = MutableStateFlow<List<DjangoModelItem>>(emptyList())
    val cantones: StateFlow<List<DjangoModelItem>> = _cantones

    private val _parroquias = MutableStateFlow<List<DjangoModelItem>>(emptyList())
    val parroquias: StateFlow<List<DjangoModelItem>> = _parroquias

    private val _sectores = MutableStateFlow<List<DjangoModelItem>>(emptyList())
    val sectores: StateFlow<List<DjangoModelItem>> = _sectores

    private val _selectedProvincia = MutableStateFlow<DjangoModelItem?>(null)
    val selectedProvincia: StateFlow<DjangoModelItem?> = _selectedProvincia

    private val _selectedCanton = MutableStateFlow<DjangoModelItem?>(null)
    val selectedCanton: StateFlow<DjangoModelItem?> = _selectedCanton

    private val _selectedParroquia = MutableStateFlow<DjangoModelItem?>(null)
    val selectedParroquia: StateFlow<DjangoModelItem?> = _selectedParroquia

    private val _selectedSector = MutableStateFlow<DjangoModelItem?>(null)
    val selectedSector: StateFlow<DjangoModelItem?> = _selectedSector

    fun setInitialData(account: Account) {
        viewModelScope.launch {
            onloadResidencia("provincias", null)
            _provincias.first { it.isNotEmpty() }

            account.idProvinciaResidencia?.let { id ->
                _selectedProvincia.value = _provincias.value.find { it.id == id }
            }

            _selectedProvincia.value?.id?.let { provinciaId ->
                onloadResidencia("cantones", provinciaId)
                _cantones.first { it.isNotEmpty() }

                account.idCantonResidencia?.let { idCanton ->
                    _selectedCanton.value = _cantones.value.find { it.id == idCanton }
                }
            }

            _selectedCanton.value?.id?.let { cantonId ->
                onloadResidencia("parroquias", cantonId)
                _parroquias.first { it.isNotEmpty() }

                account.idParroquiaResidencia?.let { idParroquia ->
                    _selectedParroquia.value = _parroquias.value.find { it.id == idParroquia }
                }
            }

            _selectedParroquia.value?.id?.let { parroquiaId ->
                onloadResidencia("sectores", parroquiaId)
                _sectores.first { it.isNotEmpty() }

                account.idSectorResidencia?.let { idSector ->
                    _selectedSector.value = _sectores.value.find { it.id == idSector }
                }
            }
        }
    }

    fun selectProvincia(provincia: DjangoModelItem) {
        _selectedProvincia.value = provincia
        _selectedCanton.value = null
        _selectedParroquia.value = null
        _selectedSector.value = null
        onloadResidencia("cantones", provincia.id)
    }

    fun selectCanton(canton: DjangoModelItem) {
        _selectedCanton.value = canton
        _selectedParroquia.value = null
        _selectedSector.value = null
        onloadResidencia("parroquias", canton.id)
    }

    fun selectParroquia(parroquia: DjangoModelItem) {
        _selectedParroquia.value = parroquia
        _selectedSector.value = null
        onloadResidencia("sectores", parroquia.id)
    }

    fun selectSector(sector: DjangoModelItem) {
        _selectedSector.value = sector
    }


    fun onloadResidencia(tipo: String, id: Int?) {
        viewModelScope.launch {
            try {
                val result = accountService.fetchResidencia(id, tipo)
                when (tipo) {
                    "provincias" -> { _provincias.value = result }
                    "cantones" -> { _cantones.value = result }
                    "parroquias" -> { _parroquias.value = result }
                    "sectores" -> { _sectores.value = result }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun updateAccount(
        idSexo: Int,
        idTipoSangre:Int,
        idEstadoCivil: Int,
        celular: Int,
        convencional: Int,
        email: String,
        idProvincia: Int,
        idCanton: Int,
        idParroquia: Int,
        idSector: Int,
        callePrincipal: String,
        calleSecundaria: String,
        numeroDomicilio: Int,
        nombrePadre: String,
        nombreMadre: String,
        idPersona: Int
    ) {
        viewModelScope.launch {
            val form = AccountForm(
                action = "updateAccount",
                idSexo = idSexo,
                idTipoSangre = idTipoSangre,
                idEstadoCivil = idEstadoCivil,
                celular = celular,
                convencional = convencional,
                email = email,
                idProvincia = idProvincia,
                idCanton = idCanton,
                idParroquia = idParroquia,
                idSector = idSector,
                callePrincipal = callePrincipal,
                calleSecundaria = calleSecundaria,
                numeroDomicilio = numeroDomicilio,
                nombrePadre = nombrePadre,
                nombreMadre = nombreMadre,
                idPersona = idPersona
            )

            val result = accountService.updateAccount(client, form)
            _response.value = result

            logInfo("prueba", "$result")
        }
    }
}