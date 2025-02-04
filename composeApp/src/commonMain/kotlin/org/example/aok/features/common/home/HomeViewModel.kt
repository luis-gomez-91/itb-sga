package org.example.aok.features.common.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.URLOpener
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.core.requestPostDispatcher
import org.example.aok.data.database.AokRepository
import org.example.aok.data.entity.User
import org.example.aok.data.network.Home
import org.example.aok.data.network.HomeResult
import org.example.aok.data.network.Periodo
import org.example.aok.data.network.Report
import org.example.aok.data.network.ReportForm
import org.example.aok.data.network.ReportResult
import org.example.aok.data.network.form.UploadPhotoForm
import org.example.aok.data.network.Error
import org.example.aok.data.network.Response
import org.example.aok.data.network.form.RequestPasswordChangeForm
import org.example.aok.features.common.login.LoginViewModel

class HomeViewModel(private val pdfOpener: URLOpener) : ViewModel() {
    val client = createHttpClient()
    val service = HomeService(client)

    private val _homeData = MutableStateFlow<Home?>(null)
    val homeData: StateFlow<Home?> = _homeData

    fun onloadHome(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchHome(id)
                logInfo("home", "$result")

                when (result) {
                    is HomeResult.Success -> {
                        _homeData.value = result.home
                        clearError()
                        _periodoSelect.value = _periodoSelect.value ?: result.home.periodos.getOrNull(0)
                    }
                    is HomeResult.Failure -> {
                        _error.value = result.error
                        _error.value!!.title = "Error al cargar datos"
                    }
                }

            } catch (e: Exception) {
                addError(error = Error(title = "Error", error = "${e.message}"))
            } finally {
                _isLoading.value = false
            }
        }
    }

//  -----------------------------------------------------------  ERROR ---------------------------------------------------------
    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun clearError() {
        _error.value = null
    }

    fun addError(error: Error) {
        _error.value = error
    }

//  ----------------------------------------------------------  LOADING ---------------------------------------------------------
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun changeLoading(value: Boolean) {
        _isLoading.value = value
    }

//  ----------------------------------------------------------  BUSQUEDA ---------------------------------------------------------
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _fastSearch = MutableStateFlow<Boolean>(true)
    val fastSearch: StateFlow<Boolean> get() = _fastSearch

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun changeFastSearch(estado: Boolean) {
        _fastSearch.value = estado
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

//  ----------------------------------------------------------  REPORTES ---------------------------------------------------------
    private val _report = MutableStateFlow<Report?>(null)

    fun onloadReport(form: ReportForm) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchReport(form)
                when (result) {
                    is ReportResult.Success -> {
                        _report.value = result.report
                        _error.value = null
                        val url = "https://sga.itb.edu.ec${_report.value!!.url}"
                        openURL(url)
                    }
                    is ReportResult.Failure -> {
                        _error.value = result.error
                        _error.value!!.title = "Error al generar reporte"
                    }

                }
            } catch (e: Exception) {
                addError(error = Error(title = "Error", error = "${e.message}"))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun openURL(url: String) {
        viewModelScope.launch {
            try {
                pdfOpener.openURL(url)
            } catch (e: Exception) {
                logInfo("alu_facturacion", "ERROR: ${e}")
            }
        }
    }


//  ----------------------------------------------------------  PAGINADO ---------------------------------------------------------
    private val _actualPage = MutableStateFlow<Int>(1)
    val actualPage: StateFlow<Int> = _actualPage

    fun pageMore() {
        _actualPage.value += 1
    }

    fun pageLess() {
        _actualPage.value -= 1
    }

    fun actualPageRestart() {
        _actualPage.value = 1
    }

    //  --------------------------------------------------- BOTTOM SHEET PERIODOS ---------------------------------------------------
    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    private val _periodoSelect = MutableStateFlow<Periodo?>(null)
    val periodoSelect: StateFlow<Periodo?> = _periodoSelect

    fun changeBottomSheet() {
        _showBottomSheet.value = !_showBottomSheet.value
    }

    fun changePeriodoSelect(periodo: Periodo) {
        _periodoSelect.value = periodo
    }

    //  --------------------------------------------------- IMAGENES ---------------------------------------------------
    private val _imageLoading = MutableStateFlow<Boolean>(false)
    val imageLoading: StateFlow<Boolean> = _imageLoading

    fun changeImageLoading(value: Boolean) {
        _imageLoading.value = value
    }

    private val _photoUploaded = MutableStateFlow(false)
    val photoUploaded: StateFlow<Boolean> = _photoUploaded

    fun resetPhotoUploadedFlag() {
        _photoUploaded.value = false
    }

    suspend fun uploadPhoto(file: ByteArray) {
        try {
            changeImageLoading(true)
            val fileAsIntList = file.map { it.toUByte().toInt() }
            val form = homeData.value?.persona?.idPersona?.let {
                UploadPhotoForm(
                    idPersona = it,
                    file = fileAsIntList,
                    action = "uploadPhoto"
                )
            }
            val result = form?.let { requestPostDispatcher(client, it) }

            _homeData.value!!.persona.foto = result?.message ?: _homeData.value!!.persona.foto
            logInfo("prueba", "${result}")
            _photoUploaded.value = true

        } catch (e: Exception) {
            val error = Error("Error", "Error inesperado: ${e.message}")
            ReportResult.Failure(error)
        } finally {
            changeImageLoading(false)
        }
    }

    //  --------------------------------------------------- BOTTOM SHEET CAMBIAR CONTRASEÑA ---------------------------------------------------
    private val _showPasswordForm = MutableStateFlow(false)
    val showPasswordForm: StateFlow<Boolean> = _showPasswordForm

    fun changeShowPasswordForm(value: Boolean) {
        _showPasswordForm.value = value
    }

    private val _previousPassword = MutableStateFlow("")
    val previousPassword: StateFlow<String> = _previousPassword

    private val _newPassword1 = MutableStateFlow("")
    val newPassword1: StateFlow<String> = _newPassword1

    private val _newPassword2 = MutableStateFlow("")
    val newPassword2: StateFlow<String> = _newPassword2

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    fun clearResponse() {
        _response.value = null
    }

    fun isChangePasswordFormValid() {
        _isFormValid.value = false
        if (_previousPassword.value != "" && _newPassword1.value != "" && _newPassword2.value != "" && _newPassword1.value == _newPassword2.value) {
            _isFormValid.value = true
        }
    }

    fun onPasswordChange(
        previousPassword: String,
        newPassword1: String,
        newPassword2: String
    ) {
        _previousPassword.value = previousPassword
        _newPassword1.value = newPassword1
        _newPassword2.value = newPassword2
        isChangePasswordFormValid()
    }

    fun requestChangePassword() {
        changeLoading(true)
        changeShowPasswordForm(false)
        viewModelScope.launch {
            try {
                changeLoading(true)
                val form = homeData.value?.persona?.let {
                    RequestPasswordChangeForm(
                        action = "requestChangePassword",
                        idPersona = it.idPersona,
                        previousPassword = _previousPassword.value,
                        newPassword = _newPassword2.value
                    )
                }

                val result = form?.let {
//                    service.requestPostDispatcher(it)
                    requestPostDispatcher(client, it)
                }

                _response.value = result
                clearError()
                logInfo("homeViewModel", "$result")


            } catch (e: Exception) {
                logInfo("homeViewModel", "Exception: ${e.message}")
                addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                changeLoading(false)
            }
        }
    }

    private val _screenSelect = MutableStateFlow<String>("screen")
    val screenSelect: StateFlow<String> = _screenSelect

    fun changeScreenSelect(value: String) {
        _screenSelect.value = value
    }


//    Biometric credentials
    private val _saveCredentialsLogin = MutableStateFlow<Boolean>(false)
    val saveCredentialsLogin: StateFlow<Boolean> = _saveCredentialsLogin

    fun changeSaveCredentialsLogin(value: Boolean) {
        _saveCredentialsLogin.value = value
    }

    suspend fun confirmCredentialsLogin(aokRepository: AokRepository, loginViewModel: LoginViewModel): Boolean {
        val user = aokRepository.userDao.getLastUser()
        val result: Boolean = user?.let {
            it.username == loginViewModel.username.value && it.password == loginViewModel.password.value
        } ?: true

        changeSaveCredentialsLogin(!result)
        return !result
    }


    fun saveCredentialsLogin(aokRepository: AokRepository, loginViewModel: LoginViewModel) {
        viewModelScope.launch {
            val user = User(
                username = loginViewModel.username.value,
                password = loginViewModel.password.value
            )
            aokRepository.userDao.upsert(user)
        }
    }
}



