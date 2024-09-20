package org.example.aok.features.common.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.Error
import org.example.aok.data.network.Home
import org.example.aok.data.network.HomeResult

class HomeViewModel : ViewModel() {
    val client = createHttpClient()
    val homeService = HomeService(client)

    private val _homeData = MutableStateFlow<Home?>(null)
    val homeData: StateFlow<Home?> = _homeData

    private val _error = MutableStateFlow<Error?>(null)
    val error: StateFlow<Error?> = _error

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _fastSearch = MutableStateFlow<Boolean>(true)
    val fastSearch: StateFlow<Boolean> get() = _fastSearch

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun changeFastSearch(estado: Boolean) {
        _fastSearch.value = estado
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun onloadHome(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = homeService.fetchHome(id)
                logInfo("home", "$result")

                when (result) {
                    is HomeResult.Success -> {
                        _homeData.value = result.home
                        _error.value = null
                    }
                    is HomeResult.Failure -> {
//                        _error.value = result.error.error ?: "An unknown error occurred"
                    }
                }

            } catch (e: Exception) {
//                _error.value = "Error loading data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}


