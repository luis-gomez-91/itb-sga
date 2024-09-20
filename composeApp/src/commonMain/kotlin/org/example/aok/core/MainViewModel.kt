package org.example.aok.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {
//    var showBottomSheet by mutableStateOf(false)
//    var showNavigationDrawer by mutableStateOf(false)

    private val _onSearch = MutableStateFlow<Boolean>(false)
    val onSearch: StateFlow<Boolean> = _onSearch

}