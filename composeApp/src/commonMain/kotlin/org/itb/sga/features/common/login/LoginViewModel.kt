package org.itb.sga.features.common.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import org.itb.sga.data.network.Error
import kotlinx.coroutines.launch
import org.itb.sga.data.network.Login
import org.itb.sga.data.network.LoginResult
import org.itb.sga.core.createHttpClient
import org.itb.sga.core.logInfo
import org.itb.sga.data.database.AokDatabase
import org.itb.sga.data.network.Response
import org.itb.sga.data.network.form.RequestPasswordRecoveryForm

class LoginViewModel(
    val biometryAuthenticator: BiometryAuthenticator
) : ViewModel(){
    val client = createHttpClient()
    val loginService = LoginService(client)

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _verPassword = MutableStateFlow(false)
    val verPassword: StateFlow<Boolean> = _verPassword

    private val _userData = MutableStateFlow<Login?>(null)
    val userData: StateFlow<Login?> = _userData

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    fun onLoginChanged(user: String, pass: String) {
        _username.value = user
        _password.value = pass
    }

    fun habilitaBoton(): Boolean {
        return !(_username.value.isBlank() || _password.value.isBlank())
    }

    fun togglePasswordVisibility() {
        _verPassword.value = _verPassword.value != true
    }

    fun clearError() {
        _error.value = null
    }

    fun changeLogin(idUsuario: Int, navHostController: NavHostController) {
        onLoginSelector(navHostController, idUsuario)
    }

    fun onLoginSelector(navController: NavHostController, userId: Int? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = loginService.fetchLogin(_username.value, _password.value, userId)
                logInfo("LoginViewModel", "$result")

                when (result) {
                    is LoginResult.Success -> {
                        _userData.value = result.login
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                        _error.value = null
                    }
                    is LoginResult.Failure -> {
                        _error.value = result.error
                    }
                }
            } catch (e: Exception) {
                logInfo("LoginViewModel", "Exception: ${e.message}")
                _error.value = Error(title = "Error", error = "${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onLogout(navController: NavHostController) {
        viewModelScope.launch {
            _username.value = ""
            _password.value = ""
            _isLoading.value = false
            _verPassword.value = false

            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationRoute ?: "login") { inclusive = true }
                launchSingleTop = true // Evita múltiples instancias de login
            }
        }
    }

//    RECUPERAR PASSWORD
    private val _userInput = MutableStateFlow<String>("")
    val userInput: StateFlow<String> = _userInput

    private val _phoneInput = MutableStateFlow<String>("")
    val phoneInput: StateFlow<String> = _phoneInput

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _showResponse = MutableStateFlow(false)
    val showResponse: StateFlow<Boolean> = _showResponse

    fun onPasswordRecoveryChange(user: String, phone: String) {
        _userInput.value = user
        _phoneInput.value = phone
    }

    fun showResponseChange(value: Boolean) {
        _showResponse.value = value
    }

    fun requestPasswordRecovery() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val form = RequestPasswordRecoveryForm(
                    action = "requestPasswordRecovery",
                    username = _userInput.value,
                    phone = _phoneInput.value,
                    password = null
                )

                val result = loginService.requestPasswordRecovery(form)
                _response.value = result
                _showResponse.value = true
                logInfo("LoginViewModel", "$result")


            } catch (e: Exception) {
                logInfo("LoginViewModel", "Exception: ${e.message}")
                _error.value = Error(title = "Error", error = "${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

//    Biometric Login
    fun tryToAuth(navHostController: NavHostController, aokDatabase: AokDatabase) = viewModelScope.launch {
        try {
            val users = aokDatabase.userDao().getAllUsers()
            if (users.first().isNotEmpty()) {
                val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                    requestTitle = "Iniciar sesión".desc(),
                    requestReason = "Coloque su dedo en el lector de huella dactilar".desc(),
                    failureButtonText = "Cancelar".desc(),
                    allowDeviceCredentials = false
                )

                if (isSuccess) {
                    val user = aokDatabase.userDao().getLastUser()
                    user?.let {
                        onLoginChanged(it.username, it.password)
                        onLoginSelector(navHostController)
//                        if (verifyPassword(it.password, "hashedPassword")) {
//                            onLoginSelector(navHostController, it.userId)
//                        }
                    }

                } else {
                    logInfo("LoginViewModel", "Autenticación fallida o cancelada")
                }
            } else {
                _error.value = Error(title = "Error", error = "No se encontraron credenciales biométricas registradas en este dispositivo. Por favor, configúralas en los ajustes del sistema.")
            }

        } catch (throwable: Throwable) {
            logInfo("LoginViewModel", "Error en autenticación biométrica: ${throwable.message}")
        }
    }

    private val _appLastVersion = MutableStateFlow<Int?>(null)
    val appLastVersion: StateFlow<Int?> = _appLastVersion

    fun fetchLastVersionApp() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = loginService.fetchLastVersionApp()
                _appLastVersion.value = result.message.toInt()
                logInfo("prueba", "$result")

            } catch (e: Exception) {
                logInfo("prueba", "Exception: ${e.message}")
                _error.value = Error(title = "Error", error = "${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
