package org.example.aok.features.common.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.data.network.Login
import org.example.aok.data.network.LoginResult
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.database.AokRepository
import org.example.aok.data.network.Response
import org.example.aok.data.network.form.RequestPasswordRecoveryForm


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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun onLoginChanged(user: String, pass: String) {
        _username.value = user
        _password.value = pass
    }

    fun habilitaBoton(): Boolean {
        return !(_username.value.isNullOrBlank() || _password.value.isNullOrBlank())
    }

    fun togglePasswordVisibility() {
        _verPassword.value = _verPassword.value != true
    }

    fun clearError() {
        _error.value = null
    }

    fun changeLogin(id: Int, nombre: String) {
        _userData.value!!.idPersona = id
        _userData.value!!.nombre = nombre
    }

    fun onLoginSelector(navController: NavHostController) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = loginService.fetchLogin(_username.value, _password.value)
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
                        _error.value = result.error.error
                    }
                }
            } catch (e: Exception) {
                logInfo("LoginViewModel", "Exception: ${e.message}")
                _error.value = "Login failed: ${e.message}"
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

            navController?.let {
                it.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
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
                _error.value = "Password recovery failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

//    Biometric Login
    fun tryToAuth(navHostController: NavHostController, aokRepository: AokRepository) = viewModelScope.launch {
        try {
            val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                requestTitle = "Iniciar sesión".desc(),
                requestReason = "Coloque su dedo en el lector de huella dactilar".desc(),
                failureButtonText = "Cancelar".desc(),
                allowDeviceCredentials = false
            )

            if (isSuccess) {
                val user = aokRepository.userDao.getLastUser()
                user?.let {
                    onLoginChanged(it.username, it.password)
                    onLoginSelector(navHostController)
                }

            } else {
                logInfo("LoginViewModel", "Autenticación fallida o cancelada")
            }

        } catch (throwable: Throwable) {
            logInfo("LoginViewModel", "Error en autenticación biométrica: ${throwable.message}")
        }
    }

}
