package org.itb.sga.features.common.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.itb.sga.core.createHttpClient
import org.itb.sga.data.network.Account
import org.itb.sga.data.network.AccountResult
import org.itb.sga.data.network.Error
import org.itb.sga.features.common.home.HomeViewModel

class AccountViewModel: ViewModel() {
    val client = createHttpClient()
    val accountService = AccountService(client)

    private val _data = MutableStateFlow<Account?>(null)
    val data: StateFlow<Account?> = _data

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
}