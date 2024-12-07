package org.example.aok.features.student.alu_documentos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.BaseViewModel
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluDocumento
import org.example.aok.data.network.AluDocumentosResult
import org.example.aok.data.network.Error
import org.example.aok.features.common.home.HomeViewModel

val client = createHttpClient()
class AluDocumentosViewModel: BaseViewModel(client) {
    val service = AluDocumentosService(client)
    override val endpoint: String = "panel_finanzas"

    private val _data = MutableStateFlow<List<AluDocumento>>(emptyList())
    val data: StateFlow<List<AluDocumento>> = _data

    private val _documentSelect = MutableStateFlow<AluDocumento?>(null)
    val documentSelect: StateFlow<AluDocumento?> = _documentSelect

    fun changeDocumentSelect(document: AluDocumento) {
        _documentSelect.value = document
    }

    fun clearDocunentSelect() {
        _documentSelect.value = null
    }

    fun onloadAluDcoumentos(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluDocumentos(id)
                when (result) {
                    is AluDocumentosResult.Success -> {
                        _data.value = result.aluDocumentos
                        homeViewModel.clearError()
                    }
                    is AluDocumentosResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                }
                logInfo("documentos", "${result}")
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }



}