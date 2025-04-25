package org.itb.sga.features.common.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.itb.sga.core.URLOpener
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.core.requestPostDispatcher
import org.itb.sga.data.database.AokDatabase
import org.itb.sga.data.entity.ParametroReporte
import org.itb.sga.data.entity.ThemePreference
import org.itb.sga.data.entity.User
import org.itb.sga.data.network.Home
import org.itb.sga.data.network.HomeResult
import org.itb.sga.data.network.Periodo
import org.itb.sga.data.network.Report
import org.itb.sga.data.network.ReportForm
import org.itb.sga.data.network.ReportResult
import org.itb.sga.data.network.form.UploadPhotoForm
import org.itb.sga.data.network.Error
import org.itb.sga.data.network.notificaciones.Notificacion
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.RequestPasswordChangeForm
import org.itb.sga.features.common.login.LoginViewModel
import kotlin.math.log

class HomeViewModel(
    private val pdfOpener: URLOpener,
    val aokDatabase: AokDatabase
) : ViewModel() {
    val client = createHttpClient()
    val service = HomeService(client)

    private val _homeData = MutableStateFlow<Home?>(null)
    val homeData: StateFlow<Home?> = _homeData

    fun clearHomeData() {
        _homeData.value = null
    }

    fun onloadHome(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = service.fetchHome(id)

                when (result) {
                    is HomeResult.Success -> {
                        _homeData.value = result.home
                        _notificaciones.value = result.home.notificaciones
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
                        val url = "https://sga.itb.edu.ec${result.report.url}"
//                        openURL(url)
                        withContext(Dispatchers.Main) { // Asegurar que se ejecuta en el hilo principal
                            openURL(url)
                        }
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
        viewModelScope.launch(Dispatchers.Main) {
            try {
                pdfOpener.openURL(url)
            } catch (e: Exception) {
                logInfo("prueba", "ERROR: ${e}")
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
    private val _showBottomSheet = MutableStateFlow(false)
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
    private val _hasShownAlert = MutableStateFlow(false)

    private val _showSaveCredentials = MutableStateFlow(false)
    val showSaveCredentials: StateFlow<Boolean> = _showSaveCredentials

    fun updateHasShownAlert(value: Boolean) {
        _hasShownAlert.value = value
    }

    fun updateShowSaveCredentials(value: Boolean) {
        _showSaveCredentials.value = value
        if (!value) _hasShownAlert.value = true // Marcar la alerta como mostrada
    }

    suspend fun confirmCredentialsLogin(loginViewModel: LoginViewModel) {
        if (_hasShownAlert.value) return // No ejecutar si ya se mostró la alerta

        val value = try {
            val user = aokDatabase.userDao().getLastUser()
            val result = user?.let {
                it.username == loginViewModel.username.value && it.password == loginViewModel.password.value
            } ?: false
            !result
        } catch (e: Exception) {
            true
        }
        _showSaveCredentials.value = value
    }

    fun saveCredentialsLogin(loginViewModel: LoginViewModel) {
        viewModelScope.launch {
            val user = User(
                username = loginViewModel.username.value,
                password = loginViewModel.password.value,
                userId = loginViewModel.userData.value!!.idUsuario,
                hashedPassword = null
            )
            aokDatabase.userDao().upsert(user)
        }
    }

    //Theme
    private val _showThemeSetting = MutableStateFlow<Boolean>(false)
    val showThemeSetting: StateFlow<Boolean> = _showThemeSetting

    private val _selectedTheme = MutableStateFlow<ThemePreference?>(null)
    val selectedTheme: StateFlow<ThemePreference?> = _selectedTheme

    fun changeshowThemeSetting(value: Boolean) {
        _showThemeSetting.value = value
    }

    fun reloadTheme() {
        viewModelScope.launch {
            val themeSelected = aokDatabase.themePreferenceDao().getSelected()
            _selectedTheme.value = themeSelected
        }
    }

    fun updateThemePreference(selectedTheme: ThemePreference, isDarkSystem: Boolean) {
        viewModelScope.launch {
            aokDatabase.themePreferenceDao().deactivateAll()
            selectedTheme.active = true
            _selectedTheme.value = selectedTheme
            if (selectedTheme.system) {
                selectedTheme.dark = isDarkSystem
            }
            aokDatabase.themePreferenceDao().upsert(selectedTheme)
        }
    }

    fun requestThemeOptions(): Flow<List<ThemePreference>> {
        return aokDatabase.themePreferenceDao().getAll()
    }

    init {
        viewModelScope.launch {
            aokDatabase.themePreferenceDao().getAll().collect { themeList ->
                if (themeList.isEmpty()) {
                    val list = listOf(
                        ThemePreference(id = 1, theme = "Tema claro", active = true, dark = false, system = false),
                        ThemePreference(id = 2, theme = "Tema oscuro", active = false, dark = true, system = false),
                        ThemePreference(id = 3, theme = "Tema del sistema", active = false, dark = false, system = true)
                    )

                    list.forEach {
                        aokDatabase.themePreferenceDao().upsert(it)
                    }
                }
            }
            val themeSelected = aokDatabase.themePreferenceDao().getSelected()
            _selectedTheme.value = themeSelected

            aokDatabase.themePreferenceDao().getAll().collect { parametro ->
                if (parametro.isEmpty()) {
                    val list = listOf(
                        ParametroReporte(id = 1, name = "Texto"),
                        ParametroReporte(id = 2, name = "Número entero"),
                        ParametroReporte(id = 3, name = "Número decimal"),
                        ParametroReporte(id = 4, name = "Verdadero o falso"),
                        ParametroReporte(id = 5, name = "Registro de datos"),
                        ParametroReporte(id = 6, name = "Fecha"),
                        ParametroReporte(id = 7, name = "Cuadro de texto"),
                    )
                    list.forEach {
                        aokDatabase.parametroReporteDao().upsert(it)
                    }
                }
            }
        }
    }

//    Notificaciones
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones

    private val _showNotifications = MutableStateFlow(false)
    val showNotifications: StateFlow<Boolean> = _showNotifications

    fun changeShowNotifications(value: Boolean) {
        _showNotifications.value = value
    }

    fun onloadNotificacionDetalle(not: Notificacion) {
        viewModelScope.launch {
            val result = homeData.value?.persona?.let { service.fetchNotificacionesDetalle(not.id, it.idPersona) }
            not.detail = result
        }
    }

}