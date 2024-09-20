package org.example.aok.features.common.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Account
import org.example.aok.data.network.AccountResult
import org.example.aok.data.network.InscripcionResult

class AccountViewModel: ViewModel() {
    val client = createHttpClient()
    val accountService = AccountService(client)

    private val _data = MutableStateFlow<Account?>(null)
    val data: StateFlow<Account?> = _data

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onloadAccount(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = accountService.fetchAccount(id)

                when (result) {
                    is AccountResult.Success -> {
                        _data.value = result.account
                        _error.value = ""
                    }
                    is AccountResult.Failure -> {
                        _error.value = result.error.error ?: "An unknown error occurred"
                    }
                }
            } catch (e: Exception) {
                _error.value = "account: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}