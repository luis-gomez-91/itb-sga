package org.example.aok.features.student.alu_documentos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.requestPostDispatcher
import org.example.aok.data.network.AluDocumento
import org.example.aok.data.network.AluDocumentosResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.Response
import org.example.aok.data.network.form.DocumentForm
import org.example.aok.features.common.home.HomeViewModel

val client = createHttpClient()
class AluDocumentosViewModel: ViewModel() {
    val service = AluDocumentosService(client)

    private val _data = MutableStateFlow<List<AluDocumento>>(emptyList())
    val data: StateFlow<List<AluDocumento>> = _data

    private val _documentSelect = MutableStateFlow<AluDocumento?>(null)
    val documentSelect: StateFlow<AluDocumento?> = _documentSelect

    private val _file = MutableStateFlow<ByteArray?>(null)
    val file: StateFlow<ByteArray?> = _file

    private val _fileName = MutableStateFlow<String?>(null)
    val fileName: StateFlow<String?> = _fileName

    private val _response = MutableStateFlow<Response?>(null)
    val response: StateFlow<Response?> = _response

    private val _action = MutableStateFlow<String?>(null)
    val action: StateFlow<String?> = _action

    fun changeDocumentSelect(document: AluDocumento?) {
        _documentSelect.value = document
    }

    fun changeAction(action: String?) {
        _action.value = action
    }

    fun changeFileName(name: String?) {
        _fileName.value = name
    }

    fun changeFile(fileSelect: ByteArray?) {
        _file.value = fileSelect
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
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

    suspend fun sendDocument(idPersona: Int) {
        val fileAsIntList = _file.value?.map { it.toUByte().toInt() }

        val form = _documentSelect.value?.let {
            fileAsIntList?.let { it1 ->
                DocumentForm(
                    action = "saveDocument",
                    idDocumento = it.idDocumento,
                    file = it1,
                    fileName = _fileName.value?: "file_${it.nombreDocumento}",
                    idPersona = idPersona
                )
            }
        }
        _response.value = form?.let { requestPostDispatcher(client, it) }
        changeFile(null)
        changeDocumentSelect(null)

    }

    suspend fun removeDocument(idPersona: Int) {
        val form = _documentSelect.value?.let {
            DocumentForm(
                action = "removeDocument",
                idDocumento = it.idDocumento,
                file = null,
                fileName = null,
                idPersona = idPersona
            )
        }
        _response.value = form?.let { requestPostDispatcher(client, it) }
        changeDocumentSelect(null)
    }
}