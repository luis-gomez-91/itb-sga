package org.example.aok.features.common.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.data.network.Login
import org.example.aok.data.network.LoginResult
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.features.common.home.HomeViewModel

class LoginViewModel : ViewModel() {
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
        if (_username.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Username or Password cannot be empty"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = loginService.fetchLogin(_username.value, _password.value)
                logInfo("LoginViewModel", "$result")

                when (result) {
                    is LoginResult.Success -> {
                        _userData.value = result.login
                        navController.navigate("home")
                        _error.value = null
                    }
                    is LoginResult.Failure -> {
                        _error.value = result.error.error ?: "An unknown error occurred"
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
            _userData.value = null
            _error.value = null

            _isLoading.value = false
            _verPassword.value = false

            navController.navigate("login") {
                popUpTo("home") { inclusive = true }  // Clear the back stack
            }
        }
    }


}
